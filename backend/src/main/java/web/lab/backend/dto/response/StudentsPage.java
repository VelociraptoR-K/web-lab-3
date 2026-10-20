package web.lab.backend.dto.response;

import web.lab.backend.dto.StudentInfoDTO;

import java.util.List;

public record StudentsPage(
    String message,
    int studentsCount,
    Integer totalPages,
    Integer page,
    Integer limit,
    List<StudentInfoDTO> students

) {
}
