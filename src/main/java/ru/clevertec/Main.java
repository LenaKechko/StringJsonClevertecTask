package ru.clevertec;

import ru.clevertec.entity.Department;
import ru.clevertec.entity.Faculty;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Subject;
import ru.clevertec.entity.Teacher;
import ru.clevertec.util.MyDeserializer;
import ru.clevertec.util.MySerializer;

import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {

        //----сериализация----
        Speciality speciality = new Speciality(UUID.randomUUID(), "math");
        MySerializer serializer = new MySerializer();
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

        Subject subject = new Subject(UUID.randomUUID(), "Programming", Map.of(1, speciality, 2, speciality));
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

        //----десериализация----

        MyDeserializer deserializer = new MyDeserializer();
        UUID uuid = UUID.randomUUID();
        String jsonString = "{\"id\" :\"" + uuid +
                "\", \"name\" : \"Name speciality\"}";

        try {
            speciality = (Speciality) deserializer.fromJsonToEntity(jsonString, Speciality.class);
            System.out.println("-----Speciality Object from JSON------");
            System.out.println(speciality);
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException |
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
            teacher = (Teacher) deserializer.fromJsonToEntity(jsonString, Teacher.class);
            System.out.println("-----Teacher Object from JSON------");
            System.out.println(teacher);
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        UUID uuidSubject = UUID.randomUUID();
        UUID uuidSpecialityFirst = UUID.randomUUID();
        UUID uuidSpecialitySecond = UUID.randomUUID();
        jsonString = "{\"id\" : \"" + uuidSubject + "\", " +
                "\"nameSubject\" : \"Graphics\", " +
                "\"specialityWithNumberOfCourse\" : " +
                "{\"1\" : {\"id\" : \"" + uuidSpecialityFirst + "\", \"name\" : \"Name speciality1\"}, " +
                "\"2\" : {\"id\" : \"" + uuidSpecialitySecond + "\", \"name\" : \"Name speciality2\"}}}";
        try {
            subject = (Subject) deserializer.fromJsonToEntity(jsonString, Subject.class);
            System.out.println("-----Subject Object from JSON------");
            System.out.println(subject);
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        UUID uuidTeacherFirst = UUID.randomUUID();
        UUID uuidTeacherSecond = UUID.randomUUID();
        UUID uuidDepartment = UUID.randomUUID();
        jsonString = "{\"id\" : \"" + uuidDepartment + "\", \"name\" : \"Application programming\", " +
                "\"subjectAndAssociationTeacher\" : {\"Subject(id=" + uuidSubject + ", nameSubject=Programming, " +
                "specialityWithNumberOfCourse=" +
                "{2=Speciality(id=" + uuidSpecialityFirst + ", name=math), " +
                "1=Speciality(id=" + uuidSpecialitySecond + ", name=math)})\" : " +
                "[{\"id\" : \"" + uuidTeacherFirst + "\", \"lastName\" : \"Kechko\", \"firstName\" : \"Elena\", " +
                "\"phoneNumber\" : \"8-029-179-81-96\", \"birthday\" : \"1991-12-07\", \"single\" :true, \"experience\" : 6}, " +
                "{\"id\" : \"" + uuidTeacherSecond + "\", \"lastName\" : \"Petrov\", \"firstName\" : \"Ivan\", " +
                "\"phoneNumber\" : \"8-029-179-81-96\", \"birthday\" : \"1991-12-07\", \"single\" : true, \"experience\" :6}]}}";
        try {
            department = (Department) deserializer.fromJsonToEntity(jsonString, Department.class);
            System.out.println("-----Department Object from JSON------");
            System.out.println(department);
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        UUID uuidFaculty = UUID.randomUUID();
        UUID uuidSpeciality = UUID.randomUUID();
        UUID uuidTeacher = UUID.randomUUID();
        jsonString = "{\"id\" : \"" + uuidFaculty + "\",\"name\" : \"Mathematics\"," +
                "\"departmentList\" : [{\"id\" : \"" + uuidDepartment + "\",\"name\" : \"Application programming\"," +
                "\"subjectAndAssociationTeacher\" : {\"Subject(id=" + uuidSubject + ", nameSubject=Programming, " +
                "specialityWithNumberOfCourse={1=Speciality(id=" + uuidSpeciality + ", name=math)})\" : " +
                "[{\"id\" : \"" + uuidTeacher + "\",\"lastName\" : \"Kechko\",\"firstName\" : \"Elena\",\"phoneNumber\" : " +
                "\"8-029-179-81-96\",\"birthday\" : \"1991-12-07\",\"single\" : true,\"experience\" : 6}]}}]}";
        try {
            faculty = (Faculty) deserializer.fromJsonToEntity(jsonString, Faculty.class);
            System.out.println("-----Faculty Object from JSON------");
            System.out.println(faculty);
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}