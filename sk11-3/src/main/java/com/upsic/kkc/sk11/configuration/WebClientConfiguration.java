package com.upsic.kkc.sk11.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.net.ssl.TrustManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.upsic.kkc.sk11.dto.Sk11AddressesDto;
import com.upsic.kkc.sk11.dto.Sk11AuthDto;
import com.upsic.kkc.sk11.dto.deserializer.LuaCollectionDeserializerWrapper;
import com.upsic.kkc.sk11.dto.deserializer.PowerGenTypeDeserializer;
import com.upsic.kkc.sk11.dto.deserializer.QReserveTypeDeserializer;
import com.upsic.kkc.sk11.enums.PowerGenType;
import com.upsic.kkc.sk11.enums.QReserveType;
import com.upsic.kkc.sk11.services.Sk11AuthService;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebClientConfiguration {

    public final Sk11Configuration sk11Configuration;

    /**
     * Простой web-client
     *
     * @return web-client
     */
    @SneakyThrows
    @Bean
    public WebClient webClient() {
        return webClientBuilder().build();
    }

    /**
     * web-client со встроенной авторизацией из sk11AuthService и кешами для токена
     *
     * @param sk11AuthService сервис аутентификации
     * @return web-client
     */
    @SneakyThrows
    @Bean
    public WebClient sk11AuthorizedWebClient(@Lazy Sk11AuthService sk11AuthService) {
        return webClientBuilder()
                .filter(addAuthHeaderAndRetryOn401(sk11AuthService))
                .build();
    }

    private ObjectMapper webClientObjectMapper() {
        ObjectMapper sk11ObjectMapper = new ObjectMapper();
        sk11ObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        sk11ObjectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        sk11ObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        sk11ObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        sk11ObjectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(PowerGenType.class, new PowerGenTypeDeserializer(sk11Configuration)); //При получении от СК-11 переводим guid в PowerGenType в зависимости от установленных properties
        simpleModule.addDeserializer(QReserveType.class, new QReserveTypeDeserializer(sk11Configuration)); //При получении от СК-11 переводим guid в QReserveType в зависимости от установленных properties
        simpleModule.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyCollectionDeserializer(DeserializationConfig config, CollectionType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                return new LuaCollectionDeserializerWrapper(deserializer);
            }
        });
        sk11ObjectMapper.registerModule(simpleModule);
        sk11ObjectMapper.registerModule(new JavaTimeModule());
        return sk11ObjectMapper;
    }

    /**
     * Общий Builder для web-клиентов
     *
     */
    private WebClient.Builder webClientBuilder() {
        ObjectMapper webClientObjectMapper = webClientObjectMapper();
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(createSslContext()))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, sk11Configuration.getConnectTimeout())
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(sk11Configuration.getReadTimeout(), TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> {
                            configurer.defaultCodecs().maxInMemorySize(1024 * sk11Configuration.getMaxInMemoryKBytes());
                            configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(webClientObjectMapper));
                            configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(webClientObjectMapper));
                        })
                        .build()
                );
    }

    private SslContext createSslContext() {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(sk11Configuration.getCert());
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(new ByteArrayInputStream(decodedBytes), null);
            TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmFactory.init(keyStore);
            return SslContextBuilder
                    .forClient()
                    .trustManager(tmFactory)
                    .build();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Функция получения токена (getAddresses закеширована, getAuth закеширована)
     *
     * @param sk11AuthService сервис аутентификации
     * @return токен
     */
    private String getToken(Sk11AuthService sk11AuthService) {
        Sk11AddressesDto sk11AddressesDto = sk11AuthService.getAddresses();
        String authPath = sk11AddressesDto.getTokenEndpointBasic();
        Sk11AuthDto sk11AuthDto = sk11AuthService.getAuth(authPath);
        return String.join(" ", sk11AuthDto.getTokenType(), sk11AuthDto.getAccessToken());
    }

    /**
     * Фильтр, добавляющий хедер авторизации к запросам
     * если запрос вернул 401 ошибку то перезапрашивает токен и повторяет запрос
     *
     * @param sk11AuthService сервис аутентификации
     * @return фильтр
     */
    private ExchangeFilterFunction addAuthHeaderAndRetryOn401(Sk11AuthService sk11AuthService) {
        ExchangeFilterFunction exchangeFilterFunction;
        try {
            exchangeFilterFunction = (request, next) -> next.exchange(ClientRequest // В фильтре перед запросом добавляем header авторизации. Токен закеширован.
                            .from(request)
                            .headers(headers -> headers.set("Authorization", getToken(sk11AuthService)))
                            .build())
                    .flatMap((Function<ClientResponse, Mono<ClientResponse>>) clientResponse -> {
                        if (clientResponse.statusCode() == HttpStatus.UNAUTHORIZED) {  //Если произошла 401 - то сбрасываем кеш токена и добавляем новый header авторизации
                            sk11AuthService.clearSk11Auth();
                            String token = getToken(sk11AuthService);
                            return next.exchange(
                                    ClientRequest
                                            .from(request)
                                            .headers(headers -> headers.replace("Authorization", Collections.singletonList(token)))
                                            .build()
                            );
                        }
                        else {
                            return Mono.just(clientResponse);
                        }
                    });
        } catch (Exception e) {
            log.error(String.format("Cause of interaction disruption: \"%s\"!", e.getMessage()));
            throw e;
        }
        return exchangeFilterFunction;
    }

}
