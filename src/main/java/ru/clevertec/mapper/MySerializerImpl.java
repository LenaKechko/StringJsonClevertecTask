package ru.clevertec.mapper;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MySerializerImpl implements MySerializer {

    @Override
    public String fromEntityToJson(Object entity) {
        StringBuilder jsonString = new StringBuilder("{\n");
        Field[] fieldsOfEntity = entity.getClass().getDeclaredFields();
        int countFields = fieldsOfEntity.length;
        for (Field currentField : fieldsOfEntity) {
            if (Modifier.isPrivate(currentField.getModifiers()))
                currentField.setAccessible(true);
            try {
                String nameField = currentField.getName();
                jsonString.append(String.format("\t\"%s\" : ", nameField));
                Object valueField = currentField.get(entity);
                jsonString.append(makeFormat(valueField));
                countFields--;
                if (countFields != 0) jsonString.append(",");
                jsonString.append("\n");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        jsonString.append("}");
        return jsonString.toString();
    }

    @Override
    public Object fromJsonToEntity(String json) {
        return null;
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
        if (obj instanceof Integer) {
            jsonString.append(String.format("%d", (Integer) obj));
        } else if (obj instanceof Double) {
            jsonString.append(String.format("%.2f", (Double) obj));
        } else if (obj instanceof String || obj instanceof UUID || obj instanceof TemporalAccessor) {
            jsonString.append(String.format("\"%s\"", obj));
        }
        return jsonString.toString();
    }

    public String makeMapFormat(Object obj) {
        StringBuilder jsonStringResult = new StringBuilder();
        jsonStringResult.append("{\n");
        ((Map<?, ?>) obj).entrySet().stream()
                .map(el -> {
                    StringBuilder temp = new StringBuilder();
                    Object key = el.getKey();
                    Object value = el.getValue();
                    temp.append(makeFormat(key));
                    temp.append(" : ");
                    temp.append(makeFormat(value));
                    return temp;
                })
                .forEach(el -> jsonStringResult.append(el).append(",\n"));
        jsonStringResult.deleteCharAt(jsonStringResult.lastIndexOf(","));
        jsonStringResult.append("}");
        return jsonStringResult.toString();
    }

    public String makeListFormat(Object obj) {
        StringBuilder jsonStringResult = new StringBuilder();
        jsonStringResult.append("[\n");
        ((List<?>) obj).stream()
                .map(this::makeFormat)
                .forEach(el -> jsonStringResult.append(el).append(",\n"));
        jsonStringResult.deleteCharAt(jsonStringResult.lastIndexOf(","));
        jsonStringResult.append("]");
        return jsonStringResult.toString();
    }

    public String makeUserDataTypeFormat(Object obj) {
        return fromEntityToJson(obj);
    }

    public boolean checkInstanceCommonType(Object obj) {
        return (obj instanceof Number || obj instanceof String || obj instanceof UUID
                || obj instanceof TemporalAccessor);
    }
}
