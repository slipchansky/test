package com.upsic.kkc.sk11.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

import java.util.Objects;

/**
 * Из lua вместо пустого списка с символами [] приходит пустой объект с символами {} что приводит к ошибке при разборе json
 * Если в начале списка приходит JsonToken.START_OBJECT - заменим его на пустую коллекцию
 */
public class LuaCollectionDeserializerWrapper extends JsonDeserializer implements ContextualDeserializer {

    private JsonDeserializer<?> deserializer;

    public LuaCollectionDeserializerWrapper(JsonDeserializer<?> deserializer) {
        super();
        this.deserializer = deserializer;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (Objects.equals(p.getCurrentToken(), JsonToken.START_OBJECT) && Objects.equals(p.nextToken(), JsonToken.END_OBJECT)) {
            return deserializer.getEmptyValue(ctxt);
        } else {
            return deserializer.deserialize(p, ctxt);
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        return new LuaCollectionDeserializerWrapper(((ContextualDeserializer) deserializer).createContextual(ctxt, property));
    }
}
