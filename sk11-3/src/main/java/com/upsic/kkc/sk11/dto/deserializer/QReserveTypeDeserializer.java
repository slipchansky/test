package com.upsic.kkc.sk11.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.upsic.kkc.sk11.configuration.Sk11Configuration;
import com.upsic.kkc.sk11.enums.QReserveType;
import com.upsic.kkc.sk11.utils.Sk11Constants;

import java.io.IOException;
import java.util.UUID;


public class QReserveTypeDeserializer extends JsonDeserializer<QReserveType> {

    private final Sk11Configuration sk11Configuration;

    public QReserveTypeDeserializer(Sk11Configuration sk11Configuration) {
        this.sk11Configuration = sk11Configuration;
    }

    @Override
    public QReserveType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String guidStr = jp.readValueAs(String.class);
        if (guidStr == null) {
            return null;
        }
        UUID guid = UUID.fromString(guidStr);
        if (guid.equals(sk11Configuration.getQLoadReserveGuid())) {
            return QReserveType.LOAD_RESERVE;
        } else if (guid.equals(sk11Configuration.getQUnloadReserveGuid())) {
            return QReserveType.UNLOAD_RESERVE;
        } else {
            throw new RuntimeException(String.format(Sk11Constants.Error.SK11_ENUM_DESERIALIZE_ERROR, guidStr));
        }
    }

}

