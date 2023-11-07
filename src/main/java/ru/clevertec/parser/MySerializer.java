package ru.clevertec.parser;

public interface MySerializer {

    String fromEntityToJson(Object entity);
    Object fromJsonToEntity(String json);
}
