package ru.clevertec.mapper;

import ru.clevertec.entity.Faculty;

public interface MySerializer<T> {

    String fromEntityToJson(T entity);
    T fromJsonToEntity(String json);
}
