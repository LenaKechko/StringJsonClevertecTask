package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    private UUID id;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private LocalDate birthday;
    private int experience;
}
