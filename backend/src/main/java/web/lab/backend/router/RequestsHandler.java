package web.lab.backend.router;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import web.lab.backend.dto.*;
import web.lab.backend.dto.response.StudentResponse;
import web.lab.backend.dto.response.StudentsPage;
import web.lab.backend.router.annotation.QUERY;
import web.lab.backend.service.StudentService;

@Path("/requests")
public class RequestsHandler {


    @Inject
    private StudentService studentService;


    @GET
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public StudentsPage getAll(@BeanParam GetFilterDTO filterParams) {
        var queryFilter = studentService.convertToQuery(filterParams);
        return studentService.getAll(queryFilter);
    }

    @QUERY
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public StudentsPage getAll(@Valid QueryFilterDTO filterParams) {
        return studentService.getAll(filterParams);
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public StudentResponse getById(@PathParam("id") Long id) {
        return studentService.getById(id);
    }


    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNew(@Valid CreateStudentDTO studentInfo) {
        return Response.status(Response.Status.CREATED)
                .entity(studentService.createNew(studentInfo))
                .build();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        studentService.deleteById(id);
        return Response.noContent().build();
    }


    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StudentResponse patchById(@PathParam("id") Long id, @Valid StudentPatchDTO patchInfo) {
        return studentService.patchById(id, patchInfo);
    }


}
