package web.lab.backend.dto.auth;

import java.time.OffsetDateTime;

public record RefreshTokenDTO(

    Long id,
    Long userId,
    String hashedToken,
    OffsetDateTime expiresAt

){
}
