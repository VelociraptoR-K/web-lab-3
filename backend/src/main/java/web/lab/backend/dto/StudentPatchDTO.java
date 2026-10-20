package web.lab.backend.dto;

import jakarta.validation.constraints.*;
import web.lab.backend.dto.validation.DateValidation;
import web.lab.backend.dto.validation.NameValidation;

import java.time.LocalDate;

public class StudentPatchDTO {

    @NameValidation
    @Size(min = 2, max = 50, message = "Length must be from 2 to 50 symbols")
    private String name;

    @NameValidation
    @Size(min = 2, max = 50, message = "Length must be from 2 to 50 symbols")
    private String surname;

    @NameValidation
    @Size(max = 50, message = "Length must be up to 50 symbols")
    private String patronymic;

    @Pattern(regexp = "^[A-Z]\\d{4}$", message = "Parameter must match the pattern")
    private String group;

    @Pattern(regexp = "^[1-9]\\d{5}$", message = "Parameter must match the pattern")
    private String isuId;

    @Min(value = 1, message = "Value must be greater than 1")
    @Max(value = 100, message = "Value must be less than 100")
    private Integer dormitoryNumber;

    @Min(value = 1, message = "Value must be greater than 1")
    @Max(value = 1000, message = "Value must be less than 1000")
    private Integer room;

    @DateValidation
    @PastOrPresent(message = "Date must be from 2000-01-01 to current date")
    private LocalDate moveInDate;

    private Boolean foreigner;
    private String notes;


    private boolean isPatronymicProvided;
    private boolean isDormitoryNumberProvided;
    private boolean isRoomProvided;
    private boolean isMoveInDateProvided;
    private boolean isNotesProvided;


    private static String trim(String value) {
        return value == null ? null : value.trim();
    }


    public void setName(String name) {
        this.name = trim(name);
    }

    public void setSurname(String surname) {
        this.surname = trim(surname);
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = trim(patronymic);
        isPatronymicProvided = true;
    }

    public void setGroup(String group) {
        this.group = trim(group);
    }

    public void setIsuId(String isuId) {
        this.isuId = trim(isuId);
    }

    public void setDormitoryNumber(Integer dormitoryNumber) {
        this.dormitoryNumber = dormitoryNumber;
        isDormitoryNumberProvided = true;
    }

    public void setRoom(Integer room) {
        this.room = room;
        isRoomProvided = true;
    }

    public void setMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
        isMoveInDateProvided = true;
    }

    public void setForeigner(Boolean foreigner) {
        this.foreigner = foreigner;
    }

    public void setNotes(String note) {
        this.notes = trim(note);
        isNotesProvided = true;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getGroup() {
        return group;
    }

    public String getIsuId() {
        return isuId;
    }

    public Integer getDormitoryNumber() {
        return dormitoryNumber;
    }

    public Integer getRoom() {
        return room;
    }

    public LocalDate getMoveInDate() {
        return moveInDate;
    }

    public Boolean getForeigner() {
        return foreigner;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isPatronymicProvided() {
        return isPatronymicProvided;
    }

    public boolean isDormitoryNumberProvided() {
        return isDormitoryNumberProvided;
    }

    public boolean isRoomProvided() {
        return isRoomProvided;
    }

    public boolean isMoveInDateProvided() {
        return isMoveInDateProvided;
    }

    public boolean isNotesProvided() {
        return isNotesProvided;
    }
}
