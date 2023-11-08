package ru.clevertec.util;

public interface MySerializer {

    String fromEntityToJson(Object entity);
    Object fromJsonToEntity(String json);
}
