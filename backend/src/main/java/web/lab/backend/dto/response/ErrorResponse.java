package web.lab.backend.dto.response;

import java.util.List;

public record ErrorResponse(
    String message,
    List<?> details
) {
}
