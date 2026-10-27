package web.lab.backend.router;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import web.lab.backend.dto.response.StudentResponse;
import web.lab.backend.dto.response.StudentsPage;
import web.lab.backend.dto.students.CreateStudentDTO;
import web.lab.backend.dto.students.GetFilterDTO;
import web.lab.backend.dto.students.QueryFilterDTO;
import web.lab.backend.dto.students.StudentPatchDTO;
import web.lab.backend.router.annotation.QUERY;
import web.lab.backend.service.StudentService;

@Path("/requests")
@SecurityRequirement(name = "bearerAuth")
public class RequestsHandler {


    @Inject
    private StudentService studentService;


    @GET
    @Path("")
    @RolesAllowed({"USER", "ADMIN"})
    @Produces({MediaType.APPLICATION_JSON})
    public StudentsPage getAll(@Valid @BeanParam GetFilterDTO filterParams) {
        var queryFilter = studentService.convertToQuery(filterParams);
        return studentService.getAll(queryFilter);
    }

    @QUERY
    @Path("")
    @RolesAllowed({"USER", "ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public StudentsPage getAll(@Valid QueryFilterDTO filterParams) {
        return studentService.getAll(filterParams);
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public StudentResponse getById(@PathParam("id") Long id) {
        return studentService.getById(id);
    }


    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    @APIResponse(
            responseCode = "201",
            description = "Студент создан",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = StudentResponse.class)
            )
    )
    public Response createNew(@Valid CreateStudentDTO studentInfo) {
        return Response.status(Response.Status.CREATED)
                .entity(studentService.createNew(studentInfo))
                .build();
    }


    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(
            responseCode = "204",
            description = "Студент удалён"
    )
    public Response deleteById(@PathParam("id") Long id) {
        studentService.deleteById(id);
        return Response.noContent().build();
    }


    @PATCH
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StudentResponse patchById(@PathParam("id") Long id, @Valid StudentPatchDTO patchInfo) {
        return studentService.patchById(id, patchInfo);
    }


}
