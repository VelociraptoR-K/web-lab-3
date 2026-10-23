package web.lab.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$")
        String username,

        @NotBlank
        String password
) {
}
