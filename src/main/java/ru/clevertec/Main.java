package ru.clevertec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.entity.Department;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Subject;
import ru.clevertec.entity.Teacher;
import ru.clevertec.entity.Faculty;
import ru.clevertec.parser.MySerializer;
import ru.clevertec.parser.MySerializerImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        Speciality speciality = new Speciality(UUID.randomUUID(), "math");
        MySerializer serializer = new MySerializerImpl();
        String result = serializer.fromEntityToJson(speciality);

        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObject = objectMapper.readValue(result, Object.class);
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        System.out.println(prettyJson);

        System.out.println("Speciality");
        System.out.println(result);

        Teacher teacher = new Teacher(UUID.randomUUID(), "Kechko", "Elena", "8-029-179-81-96",
                LocalDate.of(1991, 12, 7), true, 6);
        result = serializer.fromEntityToJson(teacher);
        System.out.println("Teacher");
        System.out.println(result);

        Subject subject = new Subject(UUID.randomUUID(), "Programming", Map.of(1,speciality, 2, speciality));
        result = serializer.fromEntityToJson(subject);
        System.out.println("Subject");
        System.out.println(result);

        Department department = new Department(UUID.randomUUID(), "Application programming",
                Map.of(subject, List.of(teacher, teacher)));
        result = serializer.fromEntityToJson(department);
        System.out.println("Department");
        System.out.println(result);

        Faculty faculty = new Faculty(UUID.randomUUID(), "Mathematics", List.of(department));
        result = serializer.fromEntityToJson(faculty);

        jsonObject = objectMapper.readValue(result, Object.class);
        prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        System.out.println(prettyJson);

        System.out.println("Faculty");
        System.out.println(result);
    }
}