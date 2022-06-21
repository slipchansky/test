package com.upsic.kkc.sk11.services;

import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.upsic.kkc.sk11.configuration.Sk11Configuration;
import com.upsic.kkc.sk11.dto.AnswerDto;
import com.upsic.kkc.sk11.dto.LuaScriptAnswerDto;
import com.upsic.kkc.sk11.dto.LuaScriptDto;
import com.upsic.kkc.sk11.dto.MeasurementValuesDto;
import com.upsic.kkc.sk11.dto.Sk11ModelDto;
import com.upsic.kkc.sk11.dto.TelemetryDeleteDto;
import com.upsic.kkc.sk11.dto.TelemetryValueDto;
import com.upsic.kkc.sk11.dto.TelemetryValueIntervalDto;
import com.upsic.kkc.sk11.dto.TelemetryValuesDeleteDto;
import com.upsic.kkc.sk11.dto.TelemetryValuesWriteDto;
import com.upsic.kkc.sk11.enums.MeasurementValueDataType;
import com.upsic.kkc.sk11.enums.TelemetryWriteType;
import com.upsic.kkc.sk11.exceptions.Sk11IntegrationRuntimeException;
import com.upsic.kkc.sk11.utils.Sk11Constants;

import reactor.core.publisher.Mono;

import static com.upsic.kkc.sk11.utils.Sk11Constants.Error.*;

import java.net.ConnectException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Сервис реализует обращения к api СК-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Sk11ApiServiceImpl implements Sk11ApiService {

    private static final String VERSIONS_PATH = "versions";

    private static final String EXECUTE_SCRIPT_PATH = "execute-script";

    private static final String GET_SNAPSHOT_PATH = "{valueType}/data/get-snapshot";

    private static final String GET_SNAPSHOT_IN_INTERVAL_PATH = "/{valueType}/{measurementValueUid}/data/in-interval";

    private static final String WRITE_PATH = "{valueType}/data/write";

    private static final String DELETE_PATH = "{valueType}/data/delete";

    private final WebClient sk11AuthorizedWebClient;

    private final Sk11AuthService sk11AuthService;

    private final Sk11Configuration sk11Configuration;

    /**
     * Формирование пути для выполнения запросов значений измерений
     *
     * @return путь для выполнения запросов значений измерений
     */
    private String getSnapshotPath(MeasurementValueDataType valueType) {
        String measurementValuesBaseAddress = sk11AuthService.getAddresses()
                .getMeasurementValuesBaseAddressTemplate()
                .replace("{apiVersion}", sk11Configuration.getApiVersion());
        String getSnapshotPath = GET_SNAPSHOT_PATH.replace("{valueType}", valueType.getPath());
        return String.join("/", measurementValuesBaseAddress, getSnapshotPath);
    }

    private String getSnapshotInIntervalPath(MeasurementValueDataType valueType, UUID measurementValueUid) {
        String measurementValuesBaseAddress = sk11AuthService.getAddresses()
                .getMeasurementValuesBaseAddressTemplate()
                .replace("{apiVersion}", sk11Configuration.getApiVersion());
        String getSnapshotPath = GET_SNAPSHOT_IN_INTERVAL_PATH
                .replace("{valueType}", valueType.getPath())
                .replace("{measurementValueUid}", measurementValueUid.toString());
        return String.join("/", measurementValuesBaseAddress, getSnapshotPath);
    }

    /**
     * Формирование пути для записи значений измерений
     *
     * @return путь для записи значений измерений
     */
    private String getWritePath(MeasurementValueDataType valueType) {
        String measurementValuesBaseAddress = sk11AuthService.getAddresses()
                .getMeasurementValuesBaseAddressTemplate()
                .replace("{apiVersion}", sk11Configuration.getApiVersion());
        String getSnapshotPath = WRITE_PATH.replace("{valueType}", valueType.getPath());
        return String.join("/", measurementValuesBaseAddress, getSnapshotPath);
    }

    /**
     * Формирование пути для удаления значений измерений
     *
     * @return путь для записи удаления измерений
     */
    private String getDeletePath(MeasurementValueDataType valueType) {
        String measurementValuesBaseAddress = sk11AuthService.getAddresses()
                .getMeasurementValuesBaseAddressTemplate()
                .replace("{apiVersion}", sk11Configuration.getApiVersion());
        String getDeletePath = DELETE_PATH.replace("{valueType}", valueType.getPath());
        return String.join("/", measurementValuesBaseAddress, getDeletePath);
    }

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
     * @return путь для выполнения lua-скриптов
     */
    private String getLuaScriptPath() {
        return String.join("/", getObjectModelsPath(), sk11Configuration.getGeneralModelGuid().toString(), VERSIONS_PATH, sk11Configuration.getModelVersion(), EXECUTE_SCRIPT_PATH);
    }

    /**
     * Получение значений телеметрии в числовом формате
     *
     * @param uids      список идентификаторов телеметрии
     * @param timestamp дата на которую нужны данные
     * @return список телеметрии по указанным UIDS
     */
    public List<TelemetryValueDto> getMeasurementValuesDouble(List<UUID> uids, Instant timestamp) {
        log.debug("get measurement values from SK-11");
        if (uids.isEmpty()) {
            return Collections.emptyList();
        }
        MeasurementValuesDto inputDto = new MeasurementValuesDto(uids, timestamp);
        LuaScriptAnswerDto<TelemetryValueDto> result = sk11AuthorizedWebClient
                .post()
                .uri(getSnapshotPath(MeasurementValueDataType.NUMERIC))
                .body(Mono.just(inputDto), MeasurementValuesDto.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<LuaScriptAnswerDto<TelemetryValueDto>>() {
                })
                .doOnError(this::errorHandler)
                .block();
        return result != null ? result.getValue() : Collections.emptyList();
    }

    /**
     * Отправка значений телеметрии в СК-11 в числовом формате
     *
     * @param telemetryValues список значений телеметрии
     */
    public AnswerDto saveMeasurementValuesDouble(List<TelemetryValueDto> telemetryValues) {
        TelemetryValuesWriteDto inputDto = TelemetryValuesWriteDto.builder()
                .writeType(TelemetryWriteType.FORCED_TO_ARCHIVE.getSk11Value())
                .values(telemetryValues)
                .build();
        return sk11AuthorizedWebClient
                .post()
                .uri(getWritePath(MeasurementValueDataType.NUMERIC))
                .body(Mono.just(inputDto), TelemetryValuesWriteDto.class)
                .retrieve()
                .bodyToMono(AnswerDto.class)
                .doOnError(this::errorHandler)
                .block();
    }

    /**
     * Удаление значений телеметрии в СК-11
     */
    @Override
    public AnswerDto deleteMeasurementValues(List<TelemetryDeleteDto> selectors) {
        TelemetryValuesDeleteDto inputDto = TelemetryValuesDeleteDto.builder()
                .selectors(selectors)
                .build();
        return sk11AuthorizedWebClient
                .post()
                .uri(getDeletePath(MeasurementValueDataType.NUMERIC))
                .body(Mono.just(inputDto), TelemetryValuesDeleteDto.class)
                .retrieve()
                .bodyToMono(AnswerDto.class)
                .doOnError(this::errorHandler)
                .block();
    }

    /**
     * Получение списка значений телеметрии за интервал времени
     */
    @Override
    public TelemetryValueIntervalDto getMeasurementValuesInInterval(UUID measurementValueUid, Instant fromTimeStamp, Instant toTimeStamp) {
        TelemetryValueIntervalDto result = sk11AuthorizedWebClient
                .get()
                .uri(getSnapshotInIntervalPath(MeasurementValueDataType.NUMERIC, measurementValueUid)
                        , uriBuilder -> uriBuilder
                                .queryParam("fromTimeStamp", fromTimeStamp)
                                .queryParam("toTimeStamp", toTimeStamp)
                                .build()
                )
                .retrieve()
                .bodyToMono(TelemetryValueIntervalDto.class)
                .doOnError(this::errorHandler)
                .block();

        if (result != null && result.getNextLink() != null) {
            //Проверено, что сервер возвращает до 400 тыс значений без ссылки. Этого более чем достаточно
            throw new Sk11IntegrationRuntimeException(Sk11Constants.Error.NEXTLINK_NOT_IMPLEMENTED, new Throwable());
        }
        return result;
    }

    /**
     * Выполнение заданного LUA-скрипта
     *
     * @param luaScriptDto - объект, содержащий LUA-скрипт
     * @param clazz        - класс для ParameterizedTypeReference
     * @param <T>          - тип объектов в результирующем списке
     * @return - список объектов указанного типа
     */
    public <T> List<T> executeLuaScript(LuaScriptDto luaScriptDto, Class<T> clazz) {
        log.debug("execute lua script: {}", luaScriptDto.getLuaScript());

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(LuaScriptAnswerDto.class, clazz);
        ParameterizedTypeReference<LuaScriptAnswerDto<T>> typeRef = ParameterizedTypeReference.forType(resolvableType.getType());

        LuaScriptAnswerDto<T> result = sk11AuthorizedWebClient
                .post()
                .uri(getLuaScriptPath())
                .body(Mono.just(luaScriptDto), LuaScriptDto.class)
                .retrieve()
                .bodyToMono(typeRef)
                .doOnError(throwable -> this.errorHandlerLuaScript(throwable, luaScriptDto))
                .block();
        return result != null ? result.getValue() : Collections.emptyList();
    }

    /**
     * Обработка ошибок обмена
     *
     * @param e Исключение
     */
    private void errorHandler(Throwable e) {
        errorHandler(e, e);
    }

    /**
     * Обработка ошибок обмена
     *
     * @param e            Исключение
     * @param luaScriptDto Объект lua скрипта
     */
    private void errorHandlerLuaScript(Throwable e, LuaScriptDto luaScriptDto) {
        String causeMessage = String.format(SK11_ADD_LUA_SCRIPT_NAME_FORMATTER, e.getMessage(), luaScriptDto.getName());
        errorHandler(e, new Throwable(causeMessage, e));
    }

    /**
     * Обработка ошибок обмена (для детализации ошибок)
     *
     * @param e     Обрабатываемое исключение
     * @param cause Причина
     */
    private void errorHandler(Throwable e, Throwable cause) {
        log.error("An error has occurred {}", e.getMessage());
        if (e instanceof Sk11IntegrationRuntimeException) { // если мы поймали Sk11IntegrationRuntimeException - значит она уже создалась ранее - отправляем её дальше
            throw (Sk11IntegrationRuntimeException) e;
        }
        if (e instanceof DecodingException) { // обработка некорректного класса
            throw new Sk11IntegrationRuntimeException(SK11_INVALID_RESULT_FORMAT, cause);
        }
        if (e instanceof WebClientResponseException.BadRequest) {
            String message = String.format(SK11_ADD_DETAILS_FORMATTER, SK11_OTHER_ERROR, ((WebClientResponseException.BadRequest) e).getResponseBodyAsString());
            throw new Sk11IntegrationRuntimeException(message, e);
        }
        if (e instanceof WebClientResponseException.UnprocessableEntity) { //обработка, если упал lua скрипт
            String causeMessageUnprocessable = String.format(SK11_ADD_DETAILS_FORMATTER, cause.getMessage(), ((WebClientResponseException.UnprocessableEntity) e).getResponseBodyAsString());
            throw new Sk11IntegrationRuntimeException(SK11_COMMAND_EXECUTING_FAIL, new Throwable(causeMessageUnprocessable, e));
        }
        if (e.getCause() instanceof ReadTimeoutException) { //обработка если запрос слишком долго выполняется
            throw new Sk11IntegrationRuntimeException(SK11_EXECUTING_TIMEOUT, cause);
        }
        if (e.getCause() instanceof ConnectException) { //обработка если нет соединения
            throw new Sk11IntegrationRuntimeException(SK11_CONNECTION_ERROR, cause);
        }
        throw new Sk11IntegrationRuntimeException(SK11_OTHER_ERROR, cause); // неопознанная ошибка
    }

    /**
     * Получение доступных моделей api СК-11
     *
     * @return список моделей
     */
    @Override
    public List<Sk11ModelDto> getModels() {
        LuaScriptAnswerDto<Sk11ModelDto> result = sk11AuthorizedWebClient
                    .get().uri(getObjectModelsPath())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<LuaScriptAnswerDto<Sk11ModelDto>>() {})
                    .block();
        return result != null ? result.getValue() : Collections.emptyList();
    }

}
