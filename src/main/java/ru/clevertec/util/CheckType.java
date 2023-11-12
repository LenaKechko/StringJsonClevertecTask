package ru.clevertec.util;

import java.util.List;

public class CheckType {

    public static boolean isInteger(List<Object> values){
        return values.stream()
                .allMatch(value -> String.valueOf(value).matches("-?\\d+"));
    }

    public static boolean isDouble(List<Object> values){
        return values.stream()
                .allMatch(value -> String.valueOf(value).matches("-?\\d+\\.?\\d+"));
    }

    public static boolean isBoolean(List<Object> values){
        return values.stream()
                .allMatch(value -> String.valueOf(value).matches("(true)|(false)"));
    }

    public static boolean isDate(List<Object> values){
        return values.stream()
                .allMatch(value -> String.valueOf(value)
                        .matches("[1-2][0-9]{3}-(1[0-2])|(0[1-9])-(3[0-1])|([0-2][0-9])"));
    }
}
