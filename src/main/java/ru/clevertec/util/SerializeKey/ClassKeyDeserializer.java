package ru.clevertec.util.SerializeKey;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class ClassKeyDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(final String key,
                                 final DeserializationContext ctxt) {
        return key;
    }
}