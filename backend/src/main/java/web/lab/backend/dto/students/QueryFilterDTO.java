package web.lab.backend.dto.students;

import jakarta.validation.constraints.Min;
import web.lab.backend.dto.validation.DateRangeValidation;

import java.time.LocalDate;
import java.util.List;

@DateRangeValidation
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

        SortByValues sortBy,
        SortByDirections order,

        @Min(value = 1, message = "Page must be at least 1")
        Integer page,
        @Min(value = 1, message = "Limit must be at least 1")
        Integer limit

) implements DateRangeValidatable {
    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    public QueryFilterDTO {
        name = trim(name);
        surname = trim(surname);
        patronymic = trim(patronymic);
        isuId = trim(isuId);

        sortBy = sortBy == null ? SortByValues.id : sortBy;
        order = order == null ? SortByDirections.up : order;

        if (limit != null && page == null) {
            page = 1;
        }
    }

    @Override
    public LocalDate getMoveInDateFrom() {
        return moveInDateFrom;
    }

    @Override
    public LocalDate getMoveInDateTo() {
        return moveInDateTo;
    }
}
