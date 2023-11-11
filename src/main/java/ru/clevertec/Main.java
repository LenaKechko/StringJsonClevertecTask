package ru.clevertec;

import ru.clevertec.entity.Department;
import ru.clevertec.entity.Faculty;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Subject;
import ru.clevertec.entity.Teacher;
import ru.clevertec.util.MySerializer;
import ru.clevertec.util.MySerializerImpl;
import ru.clevertec.util.Validator;

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

        UUID uuidSubject = UUID.randomUUID();
        UUID uuidSpecialityFirst = UUID.randomUUID();
        UUID uuidSpecialitySecond = UUID.randomUUID();
        jsonString = "{\"id\" : \"" + uuidSubject + "\", " +
                "\"nameSubject\" : \"Graphics\", " +
                "\"specialityWithNumberOfCourse\" : " +
                "{\"1\" : {\"id\" : \"" + uuidSpecialityFirst + "\", \"name\" : \"Name speciality1\"}, " +
                "\"2\" : {\"id\" : \"" + uuidSpecialitySecond + "\", \"name\" : \"Name speciality2\"}}}";
        try {
            Subject subject = (Subject) serializer.fromJsonToEntity(jsonString, Subject.class);
            System.out.println("-----Subject Object from JSON------");
            System.out.println(subject);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
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
            Department department = (Department) serializer.fromJsonToEntity(jsonString, Department.class);
            System.out.println("-----Department Object from JSON------");
            System.out.println(department);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
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
            Faculty faculty = (Faculty) serializer.fromJsonToEntity(jsonString, Faculty.class);
            System.out.println("-----Faculty Object from JSON------");
            System.out.println(faculty);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}