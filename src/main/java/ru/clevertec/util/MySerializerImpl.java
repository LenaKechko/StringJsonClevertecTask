package ru.clevertec.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MySerializerImpl implements MySerializer {
    private static final Pattern pattern = Pattern.compile("(\"\\b\\w+\" ?: ?\"?[\\w. \\-,]+\\b\"?)" +
            "|(\"\\w+\" ?: ?\\[\\{[\\w \"\\-,:]+\\}\\])" +
            "|(\"\\w+\" ?: ?\\{[\\w \\-\",:]+\\})" +
            "|(\"\\w+\" ?: ?\\{[\\{\\}\\w \\-\",:]+\\})");

    @Override
    public String fromEntityToJson(Object entity) {
        return null;
    }

    @Override
    public Object fromJsonToEntity(String json, Class<?> className) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<String, Object> parseJson = getStringObjectMap(json);
        if (className.getPackageName().equals("ru.clevertec.entity")) {
            return makeMyEntity(parseJson, className);
        }
        return null;
    }

    private static Map<String, Object> getStringObjectMap(String json) {
        Matcher matcher = pattern.matcher(json);
        Map<String, Object> parseJson = new HashMap<>();
        while (matcher.find()) {
            String[] keyValue = matcher.group().split(" ?: ?", 2);
            String key = keyValue[0].substring(1, keyValue[0].length() - 1);
            String value = keyValue[1];
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            parseJson.put(key, value);
        }
        return parseJson;
    }

    public Object returnValueByType(Type type, Object value) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (type instanceof ParameterizedType typeMap) {
            return returnValueByParametrizedType(typeMap, value);
        }
        if (type.getTypeName().contains("ru.clevertec.entity")) {
            Map<String, Object> parse = getStringObjectMap((String) value);
            return makeMyEntity(parse, ((Class<?>) type));
        }
        String nameType = ((Class<?>) type).getSimpleName();
        return switch (nameType.toLowerCase()) {
            case "string" -> value;
            case "uuid" -> UUID.fromString((String) value);
            case "integer", "int" -> Integer.parseInt((String) value);
            case "double" -> Double.parseDouble((String) value);
            case "boolean" -> Boolean.parseBoolean((String) value);
            case "localdate" -> LocalDate.parse((String) value);
            case "offsetdatetime" -> OffsetDateTime.parse((String) value);
            case "zoneddatetime" -> ZonedDateTime.parse((String) value);
            default -> null;
        };
    }

    public Object returnValueByParametrizedType(ParameterizedType type, Object value) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String nameType = type.getRawType().getTypeName();
        if (nameType.contains("Map")) {
            Map<String, Object> parse = getStringObjectMap((String) value);
            return makeMap(parse, type);
        } else if (nameType.contains("List")) return null;
        return null;
    }


    public Object makeMyEntity(Map<String, Object> parseJson, Class<?> className) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Method[] methods = className.getMethods();
        List<Method> methodsList = Arrays.stream(methods).filter(method ->
                (method.getName().startsWith("set")
                        && method.getParameterTypes().length == 1
                        && void.class.equals(method.getReturnType())
                )).toList();
        Constructor<?> constructor = className.getConstructor();

        Object myObject = constructor.newInstance();

        parseJson.forEach((key, value) -> {
            Method myMethod = methodsList.stream()
                    .filter(method -> method.getName().toLowerCase().contains(key.toLowerCase()))
                    .findFirst()
                    .orElseThrow();
            try {
//                    Class<?> parameterTypesInSetter = myMethod.getParameterTypes()[0];
                Type fieldType = className.getDeclaredField(key).getGenericType();
//                    Field field = className.getDeclaredField(key);
//                    String myFieldType = field.getType().;
                value = returnValueByType(fieldType, value);
                myMethod.invoke(myObject, value);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        return myObject;
    }

    public Object makeMap(Map<String, Object> parseJson, ParameterizedType type) {
        return parseJson.entrySet().stream()
                .map(el -> {
                    Type typeKey = type.getActualTypeArguments()[0];
                    Type typeValue = type.getActualTypeArguments()[1];
                    try {
                        Object key = returnValueByType(typeKey, el.getKey());
                        Object value = returnValueByType(typeValue, el.getValue());
                        return Map.entry(key, value);
                    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}
