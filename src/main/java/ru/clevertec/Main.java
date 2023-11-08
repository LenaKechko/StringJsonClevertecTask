package ru.clevertec;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Teacher;
import ru.clevertec.util.MySerializer;
import ru.clevertec.util.MySerializerImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        String jsonString = "{\"id\" :\"" + uuid +
                "\", \"name\" : \"Name speciality\"}";

        MySerializer serializer = new MySerializerImpl();

        try {
            Speciality speciality = (Speciality) serializer.fromJsonToEntity(jsonString, Speciality.class);
            System.out.println("-----Speciality Object from JSON------");
            System.out.println(speciality);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        jsonString = "{\"id\" : \"" + uuid + "\", " +
                "\"lastName\" : \"Surname\", " +
                "\"firstName\" : \"Name-Name\", " +
                "\"phoneNumber\" : \"8-029-111-11-11\", " +
                "\"birthday\" : \"1991-12-07\", " +
                "\"single\" : true, " +
                "\"experience\" : 5}";

        try {
            Teacher teacher = (Teacher) serializer.fromJsonToEntity(jsonString, Teacher.class);
            System.out.println("-----Teacher Object from JSON------");
            System.out.println(teacher);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}