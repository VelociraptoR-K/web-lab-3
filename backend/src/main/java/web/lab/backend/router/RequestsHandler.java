package web.lab.backend.router;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import web.lab.backend.dto.CreateStudentDTO;
import web.lab.backend.dto.StudentInfoDTO;
import web.lab.backend.dto.StudentPatchDTO;
import web.lab.backend.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

@Path("/requests")
public class RequestsHandler {


    @Inject
    private StudentRepository studentRepository;

    @GET @Path("")
    @Produces( { MediaType.APPLICATION_JSON } )
    public List<StudentInfoDTO> getAll() {
        return studentRepository.getAllStudents();
    }

    @GET @Path("/{id}")
    @Produces( MediaType.APPLICATION_JSON )
    public StudentInfoDTO getById(@PathParam("id") Long id) {
        return studentRepository.getStudentById(id);
    }

    @POST @Path("")
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public StudentInfoDTO createNew(@Valid CreateStudentDTO studentInfo) {
        return studentRepository.createStudent(studentInfo);
    }

    @DELETE @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        int result = studentRepository.deleteStudent(id);
        if (result != 1) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.noContent().build();
    }


    @PATCH @Path("/{id}")
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public StudentInfoDTO patchById(@PathParam("id") Long id, @Valid StudentPatchDTO patchInfo) {
        studentRepository.editStudent(id, patchInfo);
        return studentRepository.getStudentById(id);
    }

    /*@GET @Path("/database-check")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean databaseCheck() {
        return studentRepository.checkConnection();
    }*/


}
