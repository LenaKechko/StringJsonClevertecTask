package ru.clevertec.util;

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
import ru.clevertec.util.SerializeKey.JacksonModule;

import java.lang.reflect.InvocationTargetException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MySerializerImplTest {

    private MySerializer mySerializer;
    private ObjectMapper objectMapper;

    private MySerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new MySerializerImpl();
        mySerializer = new MySerializerImpl();
        objectMapper = new ObjectMapper().findAndRegisterModules();
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

        // when
        Subject actual = (Subject) serializer.fromJsonToEntity(jsonString, Subject.class);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void fromJsonToEntityShouldReturnDepartmentObject() throws JsonProcessingException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JacksonModule());
        Department expected = objectMapper.readValue(jsonString, Department.class);

        // when
        Department actual = (Department) serializer.fromJsonToEntity(jsonString, Department.class);

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
    void fromJsonToEntityShouldReturnFacultyObject() throws JsonProcessingException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JacksonModule());
        Faculty expected = objectMapper.readValue(jsonString, Faculty.class);

        // when
        Faculty actual = (Faculty) serializer.fromJsonToEntity(jsonString, Faculty.class);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Faculty.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Faculty.Fields.name, expected.getName());
        assertThat(actual.getDepartmentList())
                .hasSameSizeAs(expected.getDepartmentList());
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