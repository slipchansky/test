package com.upsic.kkc.services;

import com.upsic.kkc.configuration.Sk11Configuration;
import com.upsic.kkc.dto.sk11api.LuaScriptAnswerDto;
import com.upsic.kkc.dto.sk11api.LuaScriptDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;
import com.upsic.kkc.exceptions.Sk11IntegrationRuntimeException;
import static com.upsic.kkc.utils.AppConstants.ErrorDescriptions.*;

import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.Collections;
import java.util.List;

/**
 * Сервис реализует обращения к api СК-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Sk11ApiServiceImpl implements Sk11ApiService{

    private static final String VERSIONS_PATH = "versions";

    private static final String EXECUTE_SCRIPT_PATH = "execute-script";

    private final WebClient sk11AuthorizedWebClient;

    private final Sk11AuthService sk11AuthService;

    private final Sk11Configuration sk11Configuration;

    /**
     * Формирование пути для выполнения запросов к модели
     *
     * @return путь для выполнения запросов к модели
     */
    private String getObjectModelsPath() {
        return sk11AuthService.getAddresses().getObjectModelsBaseAddressTemplate().replace("{apiVersion}", sk11Configuration.getApiVersion());
    }

    /**
     * Формирование пути для выполнения lua-скриптов
     *
     * @param modelUid - идентификатор модели из СК-11
     * @return путь для выполнения lua-скриптов
     */
    private String getLuaScriptPath(String modelUid) {
        
        return String.join("/", getObjectModelsPath(), modelUid, VERSIONS_PATH, sk11Configuration.getModelVersion(), EXECUTE_SCRIPT_PATH);
    }

    /**
     * Получение доступных моделей api СК-11
     *
     * @return список моделей
     */
    public List<Sk11ModelDto> getModels() {
        LuaScriptAnswerDto<Sk11ModelDto> result = sk11AuthorizedWebClient
                .get()
                .uri(getObjectModelsPath())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<LuaScriptAnswerDto<Sk11ModelDto>>() {
                })
                .block();
        return result != null ? result.getValue() : Collections.emptyList();
    }

    /**
     * Выполнение заданного LUA-скрипта
     *
     * @param modelUid     - идентификатор модели
     * @param luaScriptDto - объект, содержащий LUA-скрипт
     * @param clazz        - класс для ParameterizedTypeReference
     * @param <T>          - тип объектов в результирующем списке
     * @return - список объектов указанного типа
     */
    public <T> List<T> executeLuaScript(String modelUid, LuaScriptDto luaScriptDto, Class<T> clazz) {
        log.debug("execute lua script: {}", luaScriptDto.getLuaScript());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(LuaScriptAnswerDto.class, clazz);
        ParameterizedTypeReference<LuaScriptAnswerDto<T>> typeRef = ParameterizedTypeReference.forType(resolvableType.getType());

        LuaScriptAnswerDto<T> result = sk11AuthorizedWebClient
                .post()
                .uri(getLuaScriptPath(modelUid))
                .body(Mono.just(luaScriptDto), LuaScriptDto.class)
                .retrieve()
                .bodyToMono(typeRef)
                .doOnError(throwable -> this.errorHandler(throwable, luaScriptDto))
                .block();
        return result != null ? result.getValue() : Collections.emptyList();
    }

    /**
     * Обработка ошибок обмена
     *
     * @param e            Исключение
     * @param luaScriptDto lua-скрипт (дя детализации ошибки)
     */
    private void errorHandler(Throwable e, LuaScriptDto luaScriptDto) {
        log.error("An error has occurred {}", e.getMessage());
        if (e instanceof Sk11IntegrationRuntimeException) { // если мы поймали Sk11IntegrationRuntimeException - значит она уже создалась ранее - отправляем её дальше
            throw (Sk11IntegrationRuntimeException) e;
        }
        String causeMessage = String.format(SK11_ADD_LUA_SCRIPT_NAME_FORMATTER, e.getMessage(), luaScriptDto.getName());
        Throwable eWithLua = new Throwable(causeMessage, e);

        if (e instanceof DecodingException) { // обработка некорректного класса
            throw new Sk11IntegrationRuntimeException(SK11_INVALID_RESULT_FORMAT, eWithLua);
        }
        if (e instanceof WebClientResponseException.UnprocessableEntity) { //обработка, если упал lua скрипт
            String causeMessageUnprocessable = String.format(SK11_ADD_DETAILS_FORMATTER, eWithLua.getMessage(), ((WebClientResponseException.UnprocessableEntity) e).getResponseBodyAsString());
            throw new Sk11IntegrationRuntimeException(SK11_COMMAND_EXECUTING_FAIL, new Throwable(causeMessageUnprocessable, e));
        }
        if (e.getCause() instanceof ReadTimeoutException) { //обработка если запрос слишком долго выполняется
            throw new Sk11IntegrationRuntimeException(SK11_EXECUTING_TIMEOUT, eWithLua);
        }
        if (e.getCause() instanceof ConnectException) { //обработка если нет соединения
            throw new Sk11IntegrationRuntimeException(SK11_CONNECTION_ERROR, eWithLua);
        }
        throw new Sk11IntegrationRuntimeException(SK11_OTHER_ERROR, eWithLua); // неопознанная ошибка
    }

}
