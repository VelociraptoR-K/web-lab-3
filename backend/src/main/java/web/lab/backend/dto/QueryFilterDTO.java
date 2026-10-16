package web.lab.backend.dto;

import java.time.LocalDate;
import java.util.List;

public record QueryFilterDTO(

        String name,
        String surname,
        String patronymic,
        String isuId,

        List<String> group,
        List<Integer> dormitoryNumber,
        List<Integer> room,

        LocalDate moveInDateFrom,
        LocalDate moveInDateTo,
        Boolean foreigner,
        String notes,

        SortByValues sortBy,
        SortByDirections order,

        Integer page,
        Integer limit

) {
    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    public QueryFilterDTO {
        name = trim(name);
        surname = trim(surname);
        patronymic = trim(patronymic);
        isuId = trim(isuId);
        notes = trim(notes);

        sortBy = sortBy == null ? SortByValues.id : sortBy;
        order = order == null ? SortByDirections.up : order;
    }
}
