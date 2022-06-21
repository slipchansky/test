package com.upsic.kkc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.upsic.kkc.Sk11DtoTestUtils;
import com.upsic.kkc.dto.sk11api.LuaScriptDto;
import com.upsic.kkc.dto.sk11api.Sk11CheckAddingLevelDto;
import com.upsic.kkc.dto.sk11api.Sk11FullMeasurementValueDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeNodeDto;

import static com.upsic.kkc.utils.AppConstants.Cache.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Сервис реализует обращения к api СК-11
 */
@Slf4j
@Service
public class Sk11ApiServiceMock implements Sk11ApiService {

    private Sk11ModelDto sk11ModelDto;
    private Sk11TreeDto sk11TreeDto; //Возвращаем корень дерева
    private  Map<UUID, List<Sk11FullMeasurementValueDto>> valuesMap;

    /**
     * Генерируем новые данные для тестов
     */
    @CacheEvict(value = {MODELS_CACHE, CHILDREN_CACHE}, allEntries = true)
    public void initMocks(){
        
        sk11ModelDto = Sk11DtoTestUtils.createSk11ModelDto();
        sk11TreeDto = Sk11DtoTestUtils.createSk11TreeDto();
        valuesMap = generateMeasurementValues();
    }

    public Sk11ApiServiceMock() {
        initMocks();
    }

    private Map<UUID, List<Sk11FullMeasurementValueDto>> generateMeasurementValues() {
        Map<UUID, List<Sk11FullMeasurementValueDto>> valuesMap = new HashMap<>();
        List<Sk11FullMeasurementValueDto> rootList = new ArrayList<>();
        sk11TreeDto.getChildren().forEach(parent -> {
            Sk11FullMeasurementValueDto dto = Sk11DtoTestUtils.createSk11FullMeasurementValueDto();
            valuesMap.put(parent.getUid(), Collections.singletonList(dto));
            rootList.add(dto);
        });
        valuesMap.put(sk11TreeDto.getUid(), rootList);
        return valuesMap;
    }

    public Sk11ModelDto getModelMock() {
        return sk11ModelDto;
    }

    public List<Sk11FullMeasurementValueDto> getMeasurementValuesMock(UUID parent) {
        return valuesMap.get(parent);
    }

    public Sk11TreeDto getTreeMock() {
        return sk11TreeDto;
    }

    public List<Sk11TreeNodeDto> getChildrenMock() {
        return sk11TreeDto.getChildren().stream()
                .map(Sk11DtoTestUtils::createSk11TreeNodeDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение доступных моделей api СК-11
     *
     * @return список моделей
     */
    public List<Sk11ModelDto> getModels() {
        return Collections.singletonList(getModelMock());
    }

    /**
     * Эмуляция выполнения lua скриптов
     *
     * @param modelUid     - идентификатор модели
     * @param luaScriptDto - объект, содержащий LUA-скрипт
     * @param clazz        - класс для ParameterizedTypeReference
     * @param <T>          тип возвращаемого значения
     * @return - сгенерированные данные
     */
    public <T> List<T> executeLuaScript(String modelUid, LuaScriptDto luaScriptDto, Class<T> clazz) {
        if (clazz.equals(Sk11TreeDto.class)) {
            return Collections.singletonList((T) getTreeMock());
        }
        if (clazz.equals(Sk11TreeNodeDto.class)) {
            if (luaScriptDto.getLuaScript().contains(getTreeMock().getUid().toString())) {
                return (List<T>) getChildrenMock();
            } else {
                return Collections.emptyList();
            }
        }
        if (clazz.equals(Sk11CheckAddingLevelDto.class)) {
            return Collections.singletonList((T) new Sk11CheckAddingLevelDto(true));
        }
        if (clazz.equals(Sk11FullMeasurementValueDto.class)) {
            if (luaScriptDto.getLuaScript().contains(getTreeMock().getUid().toString())) {
                return (List<T>) valuesMap.get(getTreeMock().getUid());
            } else {
                List<Sk11FullMeasurementValueDto> measurementValuesList = getChildrenMock().stream()
                        .filter(dto -> luaScriptDto.getLuaScript().contains(dto.getUid().toString()))
                        .map(sk11TreeNodeDto -> valuesMap.get(sk11TreeNodeDto.getUid()))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                List<Sk11FullMeasurementValueDto> selfMeasurementValuesList = getMeasurementValuesMock(getTreeMock().getUid()).stream()
                        .filter(dto -> luaScriptDto.getLuaScript().contains(dto.getUid().toString()))
                        .collect(Collectors.toList());

                measurementValuesList.addAll(selfMeasurementValuesList);
                return (List<T>) measurementValuesList;
            }
        }
        throw new RuntimeException("Sk11ApiServiceMock not implements this logic");
    }


}
