package web.lab.backend.dto;

import jakarta.ws.rs.BadRequestException;

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

        sortBy = sortBy == null ? SortByValues.id : sortBy;
        order = order == null ? SortByDirections.up : order;

        if (limit != null && page == null) {
            page = 1;
        }

        if (page != null && page < 1) {
            throw new BadRequestException("page must be at least 1");
        }

        if (limit != null && limit < 1) {
            throw new BadRequestException("limit must be at least 1");
        }

        if (moveInDateFrom == null ^ moveInDateTo == null) {
            throw new BadRequestException(
                    "moveInDateFrom and moveInDateTo must be specified together"
            );
        }

        if (moveInDateFrom != null
                && moveInDateFrom.isAfter(moveInDateTo)) {
            throw new BadRequestException(
                    "moveInDateFrom must not be later than moveInDateTo"
            );
        }
    }
}
