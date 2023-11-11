package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Speciality {
    private UUID id;
    private String name;
}
