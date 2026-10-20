package web.lab.backend.dto.response;

import web.lab.backend.dto.StudentInfoDTO;

public record StudentResponse(
        String message,
        Long id,
        StudentInfoDTO student
) {
}
