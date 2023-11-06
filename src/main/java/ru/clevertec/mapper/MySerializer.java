package ru.clevertec.mapper;

import ru.clevertec.entity.Faculty;

public interface MySerializer {

    String fromEntityToJson(Object entity);
    Object fromJsonToEntity(String json);
}
