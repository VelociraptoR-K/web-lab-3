package web.lab.backend.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import web.lab.backend.dto.response.ErrorResponse;

import java.util.List;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        var error = new ErrorResponse(
                exception.getMessage(),
                List.of()
        );
        return Response.status(404)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
