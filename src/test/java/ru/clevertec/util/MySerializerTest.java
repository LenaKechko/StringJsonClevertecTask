package ru.clevertec.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import ru.clevertec.entity.Department;
import ru.clevertec.entity.Faculty;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Subject;
import ru.clevertec.entity.Teacher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

class MySerializerTest {

    private MySerializer mySerializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mySerializer = new MySerializer();

        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @ParameterizedTest
    @MethodSource("argsProviderList")
    void fromEntityToJsonShouldReturnStringByList(List<?> myList) throws JsonProcessingException, JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = myList.get(0) instanceof TemporalAccessor;

        // given
        if (flag) {
            myList.forEach(date -> {
                try {
                    df.parse(String.valueOf(date));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        String expected = mySerializer.fromEntityToJson(myList);
        System.out.println(expected);

        // when
        if (flag) {
            objectMapper.setDateFormat(df);
        }

        String actual = objectMapper.writeValueAsString(myList);
        System.out.println(actual);

        // then
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    void fromEntityToJsonShouldReturnStringByMap() throws JsonProcessingException, JSONException {
        // given
        Map<String, String> myMap = Map.of("name", "Elena", "age", "31");
        String expected = mySerializer.fromEntityToJson(myMap);
        System.out.println(expected);

        // when
        String actual = objectMapper.writeValueAsString(myMap);
        System.out.println(actual);

        // then
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @ParameterizedTest
    @MethodSource("argsProviderMapWithList")
    void fromEntityToJsonShouldReturnStringByMapWithList(Map<?, ?> myMap) throws JsonProcessingException, JSONException {
        // given
        String expected = mySerializer.fromEntityToJson(myMap);
        System.out.println(expected);

        // when
        String actual = objectMapper.writeValueAsString(myMap);
        System.out.println(actual);

        // then
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
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

    public static Stream<Arguments> argsProviderList() {
        return Stream.of(
                Arguments.of(List.of("name", "Elena")),
                Arguments.of(List.of(7, 9, 10, 190)),
                Arguments.of(List.of(7.1, 15, 11.95)),
                Arguments.of(List.of(true, false)),
                Arguments.of(List.of(LocalDate.of(1991,12,7), LocalDate.of(2023, 3, 12)))
        );
    }

    public static Stream<Arguments> argsProviderMapWithList() {
        return Stream.of(
                Arguments.of(Map.of("name", List.of("Elena", "Anna"), "age", List.of("31", "78"))),
                Arguments.of(Map.of("weight", List.of(56.1, 47), "height", List.of(174, 160))),
                Arguments.of(Map.of(List.of(56.1, 47), "value1", List.of(174, 160), "value2"))
        );
    }
}