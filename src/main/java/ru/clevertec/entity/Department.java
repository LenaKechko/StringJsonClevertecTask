package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private UUID id;
    private String name;
    private Map<Subject, List<Teacher>> subjectAndAssociationTeacher;
}
