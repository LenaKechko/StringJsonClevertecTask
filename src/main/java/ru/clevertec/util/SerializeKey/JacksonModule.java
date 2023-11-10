package ru.clevertec.util.SerializeKey;

import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.clevertec.entity.Subject;

public class JacksonModule extends SimpleModule {
    public JacksonModule() {
        addKeyDeserializer(
                Subject.class,
                new ClassKeyDeserializer());
    }
}
