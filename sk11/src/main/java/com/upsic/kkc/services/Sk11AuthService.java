package com.upsic.kkc.services;

import com.upsic.kkc.configuration.Sk11Configuration;
import com.upsic.kkc.dto.sk11api.Sk11AddressesDto;
import com.upsic.kkc.dto.sk11api.Sk11AuthDto;
import com.upsic.kkc.exceptions.Sk11IntegrationRuntimeException;

import static com.upsic.kkc.utils.AppConstants.Cache.*;
import static com.upsic.kkc.utils.AppConstants.ErrorDescriptions.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Slf4j
@Service
@RequiredArgsConstructor
public class Sk11AuthService {

    private static final String ADDRESSES_PATH = "addresses";

    private final WebClient webClient;

    private final Sk11Configuration sk11Configuration;

    /**
     * Получение путей к api СК-11
     *
     * @return список путей
     */
    @Cacheable(ADDRESSES_CACHE)
    public Sk11AddressesDto getAddresses() {
        return webClient
                .get()
                .uri(String.join("/", sk11Configuration.getBasePath(), ADDRESSES_PATH))
                .retrieve()
                .bodyToMono(Sk11AddressesDto.class)
                .doOnError(this::errorHandler)
                .block();
    }

    /**
     * Генерация Basic токена
     *
     * @return Basic токен
     */
    private String getBasicAuthHeader() {
        String result = Base64.getEncoder().encodeToString(String.join(":", sk11Configuration.getUser(), sk11Configuration.getPassword()).getBytes(StandardCharsets.UTF_8));
        result = String.join(" ", "Basic", result);
        return result;
    }

    /**
     * Аутентификация с кешированием результата в хитром кеше. Настройки кеша в CacheConfiguration
     *
     * @param authPath путь по которому получаем токен аутентификации
     * @return токен аутентификации
     */
    @Cacheable(value = SK11_AUTH_CACHE)
    public Sk11AuthDto getAuth(String authPath) {
        log.debug("Authorization in SK-11");
        return webClient
                .post()
                .uri(authPath)
                .header("Authorization", getBasicAuthHeader())
                .retrieve()
                .bodyToMono(Sk11AuthDto.class)
                .doOnError(this::errorHandler)
                .share()
                .block();
    }

    /**
     * Обработчик ошибок аутентификации
     *
     * @param e - объект ошибки
     */
    @CacheEvict(value = SK11_AUTH_CACHE, allEntries = true)
    public void errorHandler(Throwable e) {
        log.error("An error has occurred {}", e.getMessage());
        if(e.getCause() instanceof ConnectException){
            throw new Sk11IntegrationRuntimeException(SK11_CONNECTION_ERROR, e);
        }
        if(e instanceof WebClientResponseException.Unauthorized){
            throw new Sk11IntegrationRuntimeException(SK11_AUTH_ERROR, e);
        }
        throw new Sk11IntegrationRuntimeException(SK11_OTHER_ERROR, e);
    }

    /**
     * Очистка кеша по требованию
     */
    @CacheEvict(value = SK11_AUTH_CACHE, allEntries = true)
    public void clearSk11Auth() {
        log.debug("SK11_AUTH_CACHE has been cleared");
    }

}
