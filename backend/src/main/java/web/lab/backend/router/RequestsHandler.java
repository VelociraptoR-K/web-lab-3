package web.lab.backend.router;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import web.lab.backend.dto.StudentInfoDTO;

@Path("/requests")
public class RequestsHandler {

    @GET @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public StudentInfoDTO getHandle(@PathParam("id") Integer id) {

        var response = new StudentInfoDTO();

        return response;
    }
}
