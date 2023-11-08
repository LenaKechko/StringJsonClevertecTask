package ru.clevertec.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Teacher;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
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
}