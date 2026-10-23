package web.lab.backend.dto.response;

public record TokenPairResponse(
        String accessToken,
        String refreshToken
) {
}
