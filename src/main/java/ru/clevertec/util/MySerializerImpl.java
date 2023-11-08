package ru.clevertec.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySerializerImpl implements MySerializer {

    @Override
    public String fromEntityToJson(Object entity) {
        return null;
    }

    @Override
    public Object fromJsonToEntity(String json, Class<?> className) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Pattern pattern = Pattern.compile("(\"\\b\\w+\" ?: ?\"?[\\w. \\-,]+\\b\"?)|(\"\\w+\" ?: ?[\\[{]\\{[\\w \",:]+\\}[]}])");
        Matcher matcher = pattern.matcher(json);
        Map<String, Object> parseJson = new HashMap<>();
        while (matcher.find()) {
            String[] keyValue = matcher.group().split(" ?: ?");
            String key = keyValue[0].substring(1, keyValue[0].length() - 1);
            String value = keyValue[1];
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            parseJson.put(key, value);
        }
//        parseJson.entrySet().forEach(System.out::println);
        if (className.getPackageName().equals("ru.clevertec.entity")) {
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
                    Class<?> parameterTypesInSetter = myMethod.getParameterTypes()[0];
                    value = returnValue(parameterTypesInSetter, value);
                    myMethod.invoke(myObject, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
            return myObject;
        }
        return null;
    }

    public Object returnValue(Class<?> type, Object value) {
        return switch (type.getSimpleName()) {
            case "String" -> value;
            case "UUID" -> UUID.fromString((String) value);
            case "Integer", "int" -> Integer.parseInt((String) value);
            case "Double" -> Double.parseDouble((String) value);
            case "Boolean", "boolean" -> Boolean.parseBoolean((String) value);
            case "LocalDate" -> LocalDate.parse((String) value);
            case "OffsetDateTime" -> OffsetDateTime.parse((String) value);
            case "ZonedDateTime" -> ZonedDateTime.parse((String) value);
            default -> null;
        };
    }

}
