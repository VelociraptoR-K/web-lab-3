package web.lab.backend.exception;

import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import web.lab.backend.dto.response.ErrorResponse;

import java.util.List;

@Provider
public class JsonErrorMapper
        implements ExceptionMapper<ProcessingException> {

    @Override
    public Response toResponse(ProcessingException exception) {
        if (isJsonbError(exception)) {
            var error = new ErrorResponse(
                    "Некорректный формат JSON",
                    List.of()
            );

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        }

        var error = new ErrorResponse(
                "Внутренняя ошибка сервера",
                List.of()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }

    private boolean isJsonbError(Throwable exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof JsonbException) {
                return true;
            }

            current = current.getCause();
        }
        return false;
    }
}