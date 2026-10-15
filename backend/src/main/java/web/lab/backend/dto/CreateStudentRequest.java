package web.lab.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public class CreateStudentRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    String name;

    @NotBlank
    @Size(min = 2, max = 50)
    String surname;


    @Size(max = 50)
    String patronymic;

    @NotBlank
    @Pattern(regexp = "^[A-Z]\\d{4}$")
    String group;

    @NotBlank
    @Pattern(regexp = "^[1-9]\\d{5}$")
    String isuId;

    @Min(1)
    @Max(100)
    Integer dormitoryNumber;

    @Min(1)
    @Max(1000)
    Integer room;

    @PastOrPresent
    LocalDate moveInDate;

    boolean foreigner;

    String notes;
}
