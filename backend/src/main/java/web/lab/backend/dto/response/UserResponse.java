package web.lab.backend.dto.response;

import web.lab.backend.dto.auth.UserRole;

public record UserResponse(
    Long id,
    String username,
    String email,
    UserRole role
){

}
