package web.lab.backend.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import web.lab.backend.dto.response.ErrorDetail;
import web.lab.backend.dto.response.ErrorResponse;

import java.util.List;

@Provider
public class ValidationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var details = exception.getConstraintViolations().stream()
                .map(violation -> new ErrorDetail(
                        getFieldName(violation),
                        violation.getMessage()
                ))
                .toList();

        var error = new ErrorResponse(
                "Ошибка валидации данных",
                details
        );

        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }

    private String getFieldName(ConstraintViolation<?> violation) {
        Path.Node lastNode = null;

        for (Path.Node node : violation.getPropertyPath()) {
            lastNode = node;
        }

        return lastNode == null ? null : lastNode.getName();
    }
}
