package web.lab.backend.dto.auth;


public record UserDTO(

        Long id,
        String username,
        String email,
        String hashedPassword,
        UserRole userRole
){
}
