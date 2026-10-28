package web.lab.backend.router;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(RequestsHandler.class);

    @Inject
    private StudentService studentService;


    @GET
    @Path("")
    @RolesAllowed({"USER", "ADMIN"})
    @Produces({MediaType.APPLICATION_JSON})
    public StudentsPage getAll(@Valid @BeanParam GetFilterDTO filterParams) {
        log.info("Получен HTTP-запрос GET на получение списка студентов по параметрам BeanParam.");
        var queryFilter = studentService.convertToQuery(filterParams);
        return studentService.getAll(queryFilter);
    }

    @QUERY
    @Path("")
    @RolesAllowed({"USER", "ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public StudentsPage getAll(@NotNull @Valid QueryFilterDTO filterParams) {
        log.info("Получен HTTP-запрос QUERY на получение отфильтрованного списка студентов.");
        return studentService.getAll(filterParams);
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    public StudentResponse getById(@PathParam("id") Long id) {
        log.info("Получен HTTP-запрос GET на получение данных студента с ID: {}", id);
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
    public Response createNew(@NotNull @Valid CreateStudentDTO studentInfo) {
        log.info("Получен HTTP-запрос POST на создание нового студента.");
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
        log.info("Получен HTTP-запрос DELETE на удаление студента с ID: {}", id);
        studentService.deleteById(id);
        return Response.noContent().build();
    }


    @PATCH
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StudentResponse patchById(@PathParam("id") Long id, @NotNull @Valid StudentPatchDTO patchInfo) {
        log.info("Получен HTTP-запрос PATCH на частичное обновление данных студента с ID: {}", id);
        return studentService.patchById(id, patchInfo);
    }


}

