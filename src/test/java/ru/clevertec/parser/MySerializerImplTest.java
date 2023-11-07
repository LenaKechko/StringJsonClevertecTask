package ru.clevertec.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import ru.clevertec.entity.Department;
import ru.clevertec.entity.Faculty;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Subject;
import ru.clevertec.entity.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class MySerializerImplTest {

    private MySerializer mySerializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mySerializer = new MySerializerImpl();
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void fromEntityToJsonShouldReturnStringBySpecialityObject() throws JsonProcessingException, JSONException {
        // given
        Speciality speciality = new Speciality(UUID.randomUUID(), "Some speciality");
        String expected = mySerializer.fromEntityToJson(speciality);

        // when
        String actual = objectMapper.writeValueAsString(speciality);

        // then
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    void fromEntityToJsonShouldReturnStringByTeacherObject() throws JsonProcessingException, JSONException {
        // given
        Teacher teacher = new Teacher(UUID.randomUUID(), "Surname", "Name", "8-029-111-11-11", LocalDate.now(), false, 5);
        String expected = mySerializer.fromEntityToJson(teacher);

        // when
        String actual = objectMapper.writeValueAsString(teacher);

        // then
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    void fromEntityToJsonShouldReturnStringBySubjectObject() throws JsonProcessingException, JSONException {
        // given
        Speciality speciality = new Speciality(UUID.randomUUID(), "Some speciality");
        Subject subject = new Subject(UUID.randomUUID(), "Name subject", Map.of(1, speciality, 2, speciality));
        String expected = mySerializer.fromEntityToJson(subject);

        // when
        String actual = objectMapper.writeValueAsString(subject);

        // then
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    void fromEntityToJsonShouldReturnStringByDepartmentObject() throws JsonProcessingException, JSONException {
        // given
        Speciality speciality = new Speciality(UUID.randomUUID(), "Some speciality");
        Subject subject = new Subject(UUID.randomUUID(), "Name subject", Map.of(1, speciality));

        Teacher teacher1 = new Teacher(UUID.randomUUID(), "Surname1", "Name1", "8-029-111-11-11", LocalDate.now(), false, 5);
        Teacher teacher2 = new Teacher(UUID.randomUUID(), "Surname2", "Name2", "8-029-111-11-11", LocalDate.now(), true, 20);

        Department department = new Department(UUID.randomUUID(), "Name department", Map.of(subject, List.of(teacher1, teacher2)));

        String expected = mySerializer.fromEntityToJson(department);

        // when
        String actual = objectMapper.writeValueAsString(department);

        // then
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    void fromEntityToJsonShouldReturnStringByFacultyObject() throws JsonProcessingException, JSONException {
        // given
        Speciality speciality = new Speciality(UUID.randomUUID(), "Some speciality");
        Subject subject = new Subject(UUID.randomUUID(), "Name subject", Map.of(1, speciality));

        Teacher teacher1 = new Teacher(UUID.randomUUID(), "Surname1", "Name1", "8-029-111-11-11", LocalDate.now(), false, 5);
        Teacher teacher2 = new Teacher(UUID.randomUUID(), "Surname2", "Name2", "8-029-111-11-11", LocalDate.now(), true, 20);

        Department department = new Department(UUID.randomUUID(), "Name department", Map.of(subject, List.of(teacher1, teacher2)));

        Faculty faculty = new Faculty(UUID.randomUUID(), "Department name", List.of(department));

        String expected = mySerializer.fromEntityToJson(faculty);

        // when
        String actual = objectMapper.writeValueAsString(faculty);

        // then
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }
}