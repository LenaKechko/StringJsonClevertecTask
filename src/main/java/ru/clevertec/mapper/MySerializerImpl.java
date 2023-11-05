package ru.clevertec.mapper;

public class MySerializerImpl<T> implements MySerializer<T> {

    @Override
    public String fromEntityToJson(T entity) {
        return null;
    }

    @Override
    public T fromJsonToEntity(String json) {
        return null;
    }
}
