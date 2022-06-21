package com.upsic.kkc.sk11.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.upsic.kkc.sk11.configuration.Sk11Configuration;
import com.upsic.kkc.sk11.dto.Sk11AddressesDto;
import com.upsic.kkc.sk11.dto.Sk11AuthDto;
import com.upsic.kkc.sk11.exceptions.Sk11IntegrationRuntimeException;
import com.upsic.kkc.sk11.utils.Sk11Constants;

import static com.upsic.kkc.sk11.utils.Sk11Constants.Cache.*;
import static com.upsic.kkc.sk11.utils.Sk11Constants.Error.*;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;


/**
 * Сервис выполнения запросов, не требующих аутентификации (таких, как аутентификация)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Sk11AuthService {

    private static final String ADDRESSES_PATH = "addresses";

    private final WebClient webClient;

    private final Sk11Configuration sk11Configuration;

    private final Sk11AddressService sk11AddressService;

    private final ThreadLocal<Sk11AddressesDto> addressesThreadLocal = new ThreadLocal<>();

    /**
     * Проверка соединения по указанному адресу сервера
     *
     * @param path адрес сервера
     * @return есть/нет соединения
     */
    public boolean isAddressWorked(String path) {
        try {
            getAddresses(path);
            return true;
        } catch (Sk11IntegrationRuntimeException ex) {
            return false;
        }
    }

    /**
     * Получение работоспособного пути для запросов СК-11 из настроек
     *
     * @return работоспособный путь для запросов СК-11
     */
    private String getBasePath() {
        String basePath = null;
        List<String> addresses = sk11AddressService.getSk11AddressListInternal();
        for (String address : addresses) {
            if (isAddressWorked(address)) {
                basePath = address;
                break;
            }
        }
        if (basePath == null) {
            throw new Sk11IntegrationRuntimeException(Sk11Constants.Error.NO_ALLOWED_ADDRESSES, null);
        }
        return basePath;
    }

    private Sk11AddressesDto getAddresses(String path) {
        return webClient
                .get()
                .uri(String.join("/", path, sk11Configuration.getApiPath(), ADDRESSES_PATH))
                .retrieve()
                .bodyToMono(Sk11AddressesDto.class)
                .doOnError(this::errorHandler)
                .block();
    }

    /**
     * Получение путей к api СК-11
     *
     * @return список путей
     */
    public Sk11AddressesDto getAddresses() {
        if(addressesThreadLocal.get()==null){
            log.debug("get addresses from SK-11");
            addressesThreadLocal.set(getAddresses(getBasePath()));
        }
            return addressesThreadLocal.get();
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
     * Аутентификация с кешированием результата, сбрасываем кеш при ошибке аутентификации
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
    @CacheEvict(value = {SK11_AUTH_CACHE}, allEntries = true)
    public void errorHandler(Throwable e) {
        log.error("An error has occurred {}", e.getMessage());
        if (e instanceof WebClientResponseException.Unauthorized) {
            throw new Sk11IntegrationRuntimeException(SK11_AUTH_ERROR, e);
        }
        if (e.getCause() instanceof ConnectException) {
            throw new Sk11IntegrationRuntimeException(SK11_CONNECTION_ERROR, e);
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
