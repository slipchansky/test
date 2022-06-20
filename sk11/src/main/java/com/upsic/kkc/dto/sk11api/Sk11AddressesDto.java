package com.upsic.kkc.dto.sk11api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.upsic.kkc.dto.sk11api.deserializer.Sk11AddressesDtoDeserializer;

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

    String tokenEndpointBasic;

    String tokenEndpointEmbedded;

    String coreBaseAddressTemplate;

    String objectModelsBaseAddressTemplate;

    String measurementValuesBaseAddressTemplate;

    String topologyBaseAddressTemplate;

    String switchesBaseAddressTemplate;
}
