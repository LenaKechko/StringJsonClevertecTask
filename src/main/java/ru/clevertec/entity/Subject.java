package ru.clevertec.entity;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Subject {
    private UUID id;
    private String nameSubject;
    private Map<Integer, Speciality> specialityWithNumberOfCourse;
}
