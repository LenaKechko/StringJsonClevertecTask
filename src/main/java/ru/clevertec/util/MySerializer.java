package ru.clevertec.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MySerializer {

    public String fromEntityToJson(Object entity) {
        StringBuilder jsonString = new StringBuilder();
        if (entity.getClass().getPackageName().equals("ru.clevertec.entity")) {
            jsonString.append("{");
            Field[] fieldsOfEntity = entity.getClass().getDeclaredFields();
            int countFields = fieldsOfEntity.length;
            for (Field currentField : fieldsOfEntity) {
                if (Modifier.isPrivate(currentField.getModifiers()))
                    currentField.setAccessible(true);
                try {
                    String nameField = currentField.getName();
                    jsonString.append(String.format("\"%s\" : ", nameField));
                    Object valueField = currentField.get(entity);
                    jsonString.append(makeFormat(valueField));
                    countFields--;
                    if (countFields != 0) jsonString.append(",");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            jsonString.append("}");
        } else  {
            jsonString.append(makeFormat(entity));
        }
        return jsonString.toString();
    }

    public String makeFormat(Object obj) {
        StringBuilder result = new StringBuilder();
        if (checkInstanceCommonType(obj)) {
            result.append(makeSimpleFormat(obj));
        } else if (obj instanceof List) {
            result.append(makeListFormat(obj));
        } else if (obj instanceof Map) {
            result.append(makeMapFormat(obj));
        } else result.append(makeUserDataTypeFormat(obj));
        return result.toString();
    }

    public String makeSimpleFormat(Object obj) {
        StringBuilder jsonString = new StringBuilder();
        if (obj instanceof Integer || obj instanceof Double || obj instanceof Boolean) {
            jsonString.append(obj);
        } else if (obj instanceof String || obj instanceof UUID || obj instanceof TemporalAccessor) {
            jsonString.append(String.format("\"%s\"", obj));
        }
        return jsonString.toString();
    }

    public String makeMapFormat(Object obj) {
        StringBuilder jsonStringResult = new StringBuilder();
        jsonStringResult.append("{");
        ((Map<?, ?>) obj).entrySet().stream()
                .map(el -> {
                    StringBuilder temp = new StringBuilder();
                    Object key = el.getKey();
                    Object value = el.getValue();
                    temp.append("\"").append(key).append("\"");
                    temp.append(" : ");
                    temp.append(makeFormat(value));
                    return temp;
                })
                .forEach(el -> jsonStringResult.append(el).append(","));
        jsonStringResult.deleteCharAt(jsonStringResult.lastIndexOf(","));
        jsonStringResult.append("}");
        return jsonStringResult.toString();
    }

    public String makeListFormat(Object obj) {
        StringBuilder jsonStringResult = new StringBuilder();
        jsonStringResult.append("[");
        ((List<?>) obj).stream()
                .map(this::makeFormat)
                .forEach(el -> jsonStringResult.append(el).append(","));
        jsonStringResult.deleteCharAt(jsonStringResult.lastIndexOf(","));
        jsonStringResult.append("]");
        return jsonStringResult.toString();
    }

    public String makeUserDataTypeFormat(Object obj) {
        return fromEntityToJson(obj);
    }

    public boolean checkInstanceCommonType(Object obj) {
        return (obj instanceof Number || obj instanceof String || obj instanceof UUID
                || obj instanceof TemporalAccessor || obj instanceof Boolean);
    }


}
