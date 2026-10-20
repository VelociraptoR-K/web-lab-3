package web.lab.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import web.lab.backend.dto.validation.DateRangeValidation;
import web.lab.backend.dto.validation.DateValidation;

import java.time.LocalDate;

@DateRangeValidation
public class GetFilterDTO implements DateRangeValidatable {


    private String name;
    private String surname;
    private String patronymic;
    private String isuId;
    private String group;
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

    @QueryParam("sortBy")
    @DefaultValue("id")
    private SortByValues sortBy;
    @QueryParam("order")
    @DefaultValue("up")
    private SortByDirections order;

    @QueryParam("page")
    @Min(value = 1, message = "Page must be at least 1")
    private Integer page;
    @QueryParam("limit")
    @Min(value = 1, message = "Limit must be at least 1")
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

    public String getIsuId() {
        return isuId;
    }

    @QueryParam("isuId")
    public void setIsuId(String isuId) {
        this.isuId = trim(isuId);
    }

    public String getGroup() {
        return group;
    }

    @QueryParam("group")
    public void setGroup(String group) { this.group = trim(group); }

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
