package ru.clevertec.util;

import java.lang.reflect.InvocationTargetException;

public interface MySerializer {

    String fromEntityToJson(Object entity);
    Object fromJsonToEntity(String json, Class<?> className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
