package com.upsic.kkc.sk11.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.upsic.kkc.sk11.dto.deserializer.Sk11AddressesDtoDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Получение базовых путей для авторизации и работы с моделью
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = Sk11AddressesDtoDeserializer.class)
public class Sk11AddressesDto {

    private String tokenEndpointBasic;

    private String tokenEndpointEmbedded;

    private String coreBaseAddressTemplate;

    private String objectModelsBaseAddressTemplate;

    private String measurementValuesBaseAddressTemplate;

    private String topologyBaseAddressTemplate;

    private String switchesBaseAddressTemplate;
}
