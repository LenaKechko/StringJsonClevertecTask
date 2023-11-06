package ru.clevertec;

import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Subject;
import ru.clevertec.entity.Teacher;
import ru.clevertec.mapper.MySerializer;
import ru.clevertec.mapper.MySerializerImpl;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Speciality speciality = new Speciality(UUID.randomUUID(), "math");
        MySerializer serializer = new MySerializerImpl();
        String result = serializer.fromEntityToJson(speciality);
        System.out.println("Speciality");
        System.out.println(result);

        Teacher teacher = new Teacher(UUID.randomUUID(), "Kechko", "Elena", "8-029-179-81-96",
                LocalDate.of(1991, 12, 7), 6);
        result = serializer.fromEntityToJson(teacher);
        System.out.println("Teacher");
        System.out.println(result);

        Subject subject = new Subject(UUID.randomUUID(), "Programming", Map.of(1,speciality, 2, speciality));
        result = serializer.fromEntityToJson(subject);
        System.out.println("Subject");
        System.out.println(result);

    }
}