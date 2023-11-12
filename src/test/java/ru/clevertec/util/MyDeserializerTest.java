package ru.clevertec.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.clevertec.entity.Department;
import ru.clevertec.entity.Faculty;
import ru.clevertec.entity.Speciality;
import ru.clevertec.entity.Subject;
import ru.clevertec.entity.Teacher;
import ru.clevertec.util.SerializeKey.JacksonModule;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MyDeserializerTest {

    private MyDeserializer myDeserializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        myDeserializer = new MyDeserializer();
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @ParameterizedTest
    @ValueSource(strings = {"[\"id\", \"name\"]",
            "[7, 9, 10, 190]",
            "[7.1, 15.0, 11.95]",
            "[true, false]",
            "[\"1991-12-07\", \"1992-03-12\"]"})
    void fromJsonToEntityShouldReturnList(String jsonString) throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        List<?> expected = objectMapper.readValue(jsonString, List.class);

        // when
        List<?> actual = (List<?>) myDeserializer.fromJsonToEntity(jsonString, List.class);

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"id\" : \"123-456789\", \"name\" : \"Name speciality\"}",
            "{\"id\" : 1, \"name\" : 2}",
            "{\"id\" : false, \"name\" : true}"})
    void fromJsonToEntityShouldReturnMap(String jsonString) throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        Map<?, ?> expected = objectMapper.readValue(jsonString, Map.class);

        // when
        Map<?, ?> actual = (Map<?, ?>) myDeserializer.fromJsonToEntity(jsonString, Map.class);

        // then
        assertEquals(expected, actual);
    }


    @Test
    void fromJsonToEntityShouldReturnSpecialityObject() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        UUID uuid = UUID.randomUUID();
        String jsonString = "{\"id\" : \"" + uuid +
                "\", \"name\" : \"Name speciality\"}";
        Speciality expected = objectMapper.readValue(jsonString, Speciality.class);

        // when
        Speciality actual = (Speciality) myDeserializer.fromJsonToEntity(jsonString, Speciality.class);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void fromJsonToEntityShouldReturnTeacherObject() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        UUID uuid = UUID.randomUUID();
        String jsonString = "{\"id\" : \"" + uuid + "\", " +
                "\"lastName\" : \"Surname\", " +
                "\"firstName\" : \"Name-Name\", " +
                "\"phoneNumber\" : \"8-029-111-11-11\", " +
                "\"birthday\" : \"1991-12-07\", " +
                "\"single\" : true, " +
                "\"experience\" : 5}";
        Teacher expected = objectMapper.readValue(jsonString, Teacher.class);

        // when
        Teacher actual = (Teacher) myDeserializer.fromJsonToEntity(jsonString, Teacher.class);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void fromJsonToEntityShouldReturnSubjectObject() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
        Subject expected = objectMapper.readValue(jsonString, Subject.class);

        // when
        Subject actual = (Subject) myDeserializer.fromJsonToEntity(jsonString, Subject.class);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void fromJsonToEntityShouldReturnDepartmentObject() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        UUID uuidTeacher = UUID.randomUUID();
        UUID uuidDepartment = UUID.randomUUID();
        UUID uuidSubject = UUID.randomUUID();
        UUID uuidSpeciality = UUID.randomUUID();
        String jsonString = "{\"id\" : \"" + uuidDepartment + "\", \"name\" : \"Application programming\", " +
                "\"subjectAndAssociationTeacher\" : {\"Subject(id=" + uuidSubject + ", nameSubject=Programming, " +
                "specialityWithNumberOfCourse=" +
                "{1=Speciality(id=" + uuidSpeciality + ", name=math)})\" : " +
                "[{\"id\" : \"" + uuidTeacher + "\", \"lastName\" : \"Kechko\", \"firstName\" : \"Elena\", " +
                "\"phoneNumber\" : \"8-029-179-81-96\", \"birthday\" : \"1991-12-07\", \"single\" : true, \"experience\" : 6}]}}";
        objectMapper.registerModule(new JacksonModule());
        Department expected = objectMapper.readValue(jsonString, Department.class);

        // when
        Department actual = (Department) myDeserializer.fromJsonToEntity(jsonString, Department.class);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Department.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Department.Fields.name, expected.getName());

        Map<Subject, List<Teacher>> expectedMap = expected.getSubjectAndAssociationTeacher();
        Map<Subject, List<Teacher>> actualMap = actual.getSubjectAndAssociationTeacher();
        assertThat(actualMap.keySet())
                .hasSameSizeAs(expectedMap.keySet());
        assertThat(actualMap.values())
                .hasSameElementsAs(expectedMap.values());
    }

    @Test
    void fromJsonToEntityShouldReturnFacultyObject() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        UUID uuidFaculty = UUID.randomUUID();
        UUID uuidTeacher = UUID.randomUUID();
        UUID uuidDepartment = UUID.randomUUID();
        UUID uuidSubject = UUID.randomUUID();
        UUID uuidSpeciality = UUID.randomUUID();
        String jsonString = "{\"id\" : \"" + uuidFaculty + "\",\"name\" : \"Mathematics\"," +
                "\"departmentList\" : [{\"id\" : \"" + uuidDepartment + "\",\"name\" : \"Application programming\"," +
                "\"subjectAndAssociationTeacher\" : {\"Subject(id=" + uuidSubject + ", nameSubject=Programming, " +
                "specialityWithNumberOfCourse={1=Speciality(id=" + uuidSpeciality + ", name=math)})\" : " +
                "[{\"id\" : \"" + uuidTeacher + "\",\"lastName\" : \"Kechko\",\"firstName\" : \"Elena\",\"phoneNumber\" : \"8-029-179-81-96\",\"birthday\" : \"1991-12-07\",\"single\" : true,\"experience\" : 6}]}}]}";
        objectMapper.registerModule(new JacksonModule());
        Faculty expected = objectMapper.readValue(jsonString, Faculty.class);

        // when
        Faculty actual = (Faculty) myDeserializer.fromJsonToEntity(jsonString, Faculty.class);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Faculty.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Faculty.Fields.name, expected.getName());
        assertThat(actual.getDepartmentList())
                .hasSameSizeAs(expected.getDepartmentList());
    }
}