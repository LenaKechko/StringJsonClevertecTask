package ru.clevertec.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
            "|(\"\\w+\" ?: ?\\[\\{[\\w \"\\-,:]+\\}])" +
            "|(\"\\w+\" ?: ?\\{[\\w \\-\",:]+\\})" +
            "|(\"\\w+\" ?: ?\\{[{}\\w \\-\",:]+\\})" +
            "|(\"\\w+\" ?: ?\\{[{()}\\[\\]\\w= \\-\",:]+\\})" +
            "|(\"[\\w =\\-(){},]+\" ?: ?\\[\\{[{()}\\[\\]\\w= \\-\",:]+\\}\\])");

    private static final Pattern patternList = Pattern.compile("\\{[\\[\\](){}\\w= \\-\",:]+\\}");

    @Override
    public String fromEntityToJson(Object entity) {
        return null;
    }

    @Override
    public Object fromJsonToEntity(String json, Class<?> className) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (Validator.isValidBrackets(json) && Validator.isValidQuote(json)) {
            Map<String, Object> parseJson = getStringObjectMap(json);
            if (className.getPackageName().equals("ru.clevertec.entity")) {
                return makeMyEntity(parseJson, className);
            } else {
                returnValueByType((Type) className, json);
            }
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

    private static List<Object> getStringObjectList(String json) {
        Matcher matcher = patternList.matcher(json);
        List<Object> parseJson = new ArrayList<>();
        while (matcher.find()) {
            parseJson.add(matcher.group());
        }
        return parseJson;
    }

    private static Map<String, Object> getStringObjectEntity(String json, Class<?> classObject) {
        int countOfFields = classObject.getDeclaredFields().length;
        json = json.substring(classObject.getSimpleName().length() + 1);
        if (json.endsWith(")") || json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }
        return Arrays.stream(json.split(", ", countOfFields))
                .map(el -> {
                            String[] keyValue = el.split("=", 2);
                            String key = keyValue[0];
                            String value = keyValue[1];
                            return Map.entry(key, value);
                        }
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Map<String, Object> getStringObjectEntityMap(String json) {
        json = json.substring(1, json.length() - 1);
        return Arrays.stream(json.split("\\), "))
                .map(el -> {
                            String[] keyValue = el.split("=", 2);
                            String key = keyValue[0];
                            String value = keyValue[1];
                            return Map.entry(key, value);
                        }
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Object returnValueByType(Type type, Object value) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (type instanceof ParameterizedType typeMap) {
            return returnValueByParametrizedType(typeMap, value);
        }
        if (type.getTypeName().contains("ru.clevertec.entity")) {
            Map<String, Object> parse = getStringObjectMap((String) value);
            if (parse.isEmpty()) {
                parse = getStringObjectEntity((String) value, ((Class<?>) type));
            }
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

    public Object returnValueByParametrizedType(ParameterizedType type, Object value) {
        String nameType = type.getRawType().getTypeName();
        if (nameType.contains("Map")) {
            Map<String, Object> parse = getStringObjectMap((String) value);
            if (parse.isEmpty()) parse = getStringObjectEntityMap((String) value);
            return makeMap(parse, type);
        } else if (nameType.contains("List")) {
            List<Object> parse = getStringObjectList((String) value);
            return makeList(parse, type.getActualTypeArguments()[0]);
        }
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
                Type fieldType = className.getDeclaredField(key).getGenericType();
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

    public Object makeList(List<Object> parseJson, Type type) {
        return parseJson.stream()
                .map(el -> {
                    try {
                        return returnValueByType(type, el);
                    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
