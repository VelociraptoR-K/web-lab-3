package web.lab.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import web.lab.backend.dto.response.ErrorResponse;

import java.util.List;

@Provider
public class OveralExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger log = LoggerFactory.getLogger(OveralExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        log.error("Произошла непредвиденная ошибка на сервере: ", exception);

        var error = new ErrorResponse(
                "Внутренняя ошибка сервера",
                List.of()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
