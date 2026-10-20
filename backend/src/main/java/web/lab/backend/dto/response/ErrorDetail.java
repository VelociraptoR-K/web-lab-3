package web.lab.backend.dto.response;

public record ErrorDetail(
        String fieldName,
        String message
) {
}
