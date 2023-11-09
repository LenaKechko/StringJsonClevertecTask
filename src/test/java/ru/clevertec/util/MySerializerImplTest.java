package ru.clevertec.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Subject;
import ru.clevertec.entity.Teacher;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MySerializerImplTest {

    private MySerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new MySerializerImpl();
    }

    @Test
    void fromJsonToEntityShouldReturnSpecialityObject() throws JsonProcessingException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        UUID uuid = UUID.randomUUID();
        String jsonString = "{\"id\" : \"" + uuid +
                "\", \"name\" : \"Name speciality\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Speciality expected = objectMapper.readValue(jsonString, Speciality.class);

        // when
        Speciality actual = (Speciality) serializer.fromJsonToEntity(jsonString, Speciality.class);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void fromJsonToEntityShouldReturnTeacherObject() throws JsonProcessingException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        UUID uuid = UUID.randomUUID();
        String jsonString = "{\"id\" : \"" + uuid + "\", " +
                "\"lastName\" : \"Surname\", " +
                "\"firstName\" : \"Name-Name\", " +
                "\"phoneNumber\" : \"8-029-111-11-11\", " +
                "\"birthday\" : \"1991-12-07\", " +
                "\"single\" : true, " +
                "\"experience\" : 5}";
        ObjectMapper objectMapper = new ObjectMapper();
        Teacher expected = objectMapper.readValue(jsonString, Teacher.class);

        // when
        Teacher actual = (Teacher) serializer.fromJsonToEntity(jsonString, Teacher.class);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void fromJsonToEntityShouldReturnSubjectObject() throws JsonProcessingException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        UUID uuidSubject = UUID.randomUUID();
        UUID uuidSpecialityFirst = UUID.randomUUID();
        UUID uuidSpecialitySecond = UUID.randomUUID();
        String jsonString = "{\"id\" : \"" + uuidSubject + "\", " +
                "\"nameSubject\" : \"Graphics\", " +
                "\"specialityWithNumberOfCourse\" : " +
                "{\"1\" : {\"id\" : \"" + uuidSpecialityFirst + "\", \"name\" : \"Name speciality1\"}, " +
                "\"2\" : {\"id\" : \"" + uuidSpecialitySecond + "\", \"name\" : \"Name speciality2\"}" +
                "}}";
        ObjectMapper objectMapper = new ObjectMapper();
        Subject expected = objectMapper.readValue(jsonString, Subject.class);
        System.out.println(expected);

        // when
        Subject actual = (Subject) serializer.fromJsonToEntity(jsonString, Subject.class);

        // then
        assertEquals(expected, actual);
    }

//    {"id" : "3da5e459-4ae8-4588-98a2-64ee2472494b","nameSubject" : "Name subject",
//    "specialityWithNumberOfCourse" :
//    {"2" : {"id" : "3fe79e84-4544-4a5a-9ef6-4462b0dd01be","name" : "Some speciality"},
//    "1" : {"id" : "3fe79e84-4544-4a5a-9ef6-4462b0dd01be","name" : "Some speciality"}}}
}