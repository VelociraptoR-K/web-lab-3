package web.lab.backend.dto.response;

import web.lab.backend.dto.students.StudentInfoDTO;

public record StudentResponse(
        String message,
        Long id,
        StudentInfoDTO student
) {
}
