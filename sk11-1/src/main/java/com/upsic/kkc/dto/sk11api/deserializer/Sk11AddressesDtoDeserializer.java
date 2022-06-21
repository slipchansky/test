package com.upsic.kkc.dto.sk11api.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.upsic.kkc.dto.sk11api.Sk11AddressesDto;

import java.io.IOException;

public class Sk11AddressesDtoDeserializer extends StdDeserializer<Sk11AddressesDto> {

    /**
     * Конструктор по умолчанию. never used обманывает - без него работать не будет
     */
    public Sk11AddressesDtoDeserializer() {
        this(null);
    }

    public Sk11AddressesDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Sk11AddressesDto deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        JsonNode jsonNode = jp.getCodec().readTree(jp);
        Sk11AddressesDto sk11AddressesDto = new Sk11AddressesDto();
        sk11AddressesDto.setTokenEndpointBasic(jsonNode.get("auth").get("tokenEndpointBasic").textValue());
        sk11AddressesDto.setTokenEndpointEmbedded(jsonNode.get("auth").get("tokenEndpointEmbedded").textValue());
        sk11AddressesDto.setCoreBaseAddressTemplate(jsonNode.get("apis").get("core").get("baseAddressTemplate").textValue());
        sk11AddressesDto.setObjectModelsBaseAddressTemplate(jsonNode.get("apis").get("object-models").get("baseAddressTemplate").textValue());
        sk11AddressesDto.setMeasurementValuesBaseAddressTemplate(jsonNode.get("apis").get("measurement-values").get("baseAddressTemplate").textValue());
        sk11AddressesDto.setTopologyBaseAddressTemplate(jsonNode.get("apis").get("topology").get("baseAddressTemplate").textValue());
        sk11AddressesDto.setSwitchesBaseAddressTemplate(jsonNode.get("apis").get("switches").get("baseAddressTemplate").textValue());
        return sk11AddressesDto;
    }

}
