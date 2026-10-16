package web.lab.backend.dto;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import web.lab.backend.dto.validation.DateValidation;

import java.time.LocalDate;

public class GetFilterDTO {


    private String name;
    private String surname;
    private String patronymic;
    private String group;
    private String isuId;
    @QueryParam("dormitoryNumber")
    private Integer dormitoryNumber;
    @QueryParam("room")
    private Integer room;
    @QueryParam("moveInDateFrom")
    private LocalDate moveInDateFrom;
    @QueryParam("moveInDateTo")
    private LocalDate moveInDateTo;
    @QueryParam("foreigner")
    private Boolean foreigner;
    private String notes;

    @QueryParam("sortBy")
    @DefaultValue("id")
    private SortByValues sortBy;
    @QueryParam("order")
    @DefaultValue("up")
    private SortByDirections order;

    @QueryParam("page")
    private Integer page;
    @QueryParam("limit")
    private Integer limit;


    private static String trim(String value) {
        return value == null ? null : value.trim();
    }


    public String getName() {
        return name;
    }

    @QueryParam("name")
    public void setName(String name) {
        this.name = trim(name);
    }

    public String getSurname() {
        return surname;
    }

    @QueryParam("surname")
    public void setSurname(String surname) {
        this.surname = trim(surname);
    }

    public String getPatronymic() {
        return patronymic;
    }

    @QueryParam("patronymic")
    public void setPatronymic(String patronymic) {
        this.patronymic = trim(patronymic);
    }

    public String getGroup() {
        return group;
    }

    @QueryParam("group")
    public void setGroup(String group) {
        this.group = trim(group);
    }

    public String getIsuId() {
        return isuId;
    }

    @QueryParam("isuId")
    public void setIsuId(String isuId) {
        this.isuId = trim(isuId);
    }

    public Integer getDormitoryNumber() {
        return dormitoryNumber;
    }

    public void setDormitoryNumber(Integer dormitoryNumber) {
        this.dormitoryNumber = dormitoryNumber;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public LocalDate getMoveInDateFrom() {
        return moveInDateFrom;
    }

    public void setMoveInDateFrom(LocalDate moveInDateFrom) {
        this.moveInDateFrom = moveInDateFrom;
    }

    public LocalDate getMoveInDateTo() {
        return moveInDateTo;
    }

    public void setMoveInDateTo(LocalDate moveInDateTo) {
        this.moveInDateTo = moveInDateTo;
    }

    public Boolean getForeigner() {
        return foreigner;
    }

    public void setForeigner(Boolean foreigner) {
        this.foreigner = foreigner;
    }

    public String getNotes() {
        return notes;
    }

    @QueryParam("notes")
    public void setNotes(String notes) {
        this.notes = trim(notes);
    }

    public SortByValues getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortByValues sortBy) {
        this.sortBy = sortBy == null ? SortByValues.id : sortBy;
    }

    public SortByDirections getOrder() {
        return order;
    }

    public void setOrder(SortByDirections order) {
        this.order = order == null ? SortByDirections.up : order;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
