package web.lab.backend.router;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import web.lab.backend.dto.auth.LoginRequest;
import web.lab.backend.dto.auth.RefreshRequest;
import web.lab.backend.dto.auth.RegisterRequest;
import web.lab.backend.dto.response.AccessTokenResponse;
import web.lab.backend.dto.response.TokenPairResponse;
import web.lab.backend.dto.response.UserResponse;
import web.lab.backend.service.SecurityService;

@Path("/auth")
public class SecurityHandler {

    @Inject
    private SecurityService securityService;

    @Inject
    private JsonWebToken jwt;


    private NewCookie refreshCookie(String token, int maxAge) {
        return new NewCookie.Builder("refreshToken")
                .value(token).path("/api/auth")
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.STRICT)
                .secure(false)
                .maxAge(maxAge)
                .build();
    }


    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @APIResponse(
            responseCode = "201",
            description = "Пользователь зарегистрирован",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    public Response register(@NotNull @Valid RegisterRequest request) {
        UserResponse user = securityService.registerUser(request);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @APIResponse(
            responseCode = "200",
            description = "Access-токен. Refresh-токен устанавливается в HttpOnly cookie.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AccessTokenResponse.class)
            )
    )
    public Response login(@NotNull @Valid LoginRequest request) {
        TokenPairResponse newTokens = securityService.loginUser(request);

        return Response.ok(new AccessTokenResponse(newTokens.accessToken()))
                .cookie(refreshCookie(newTokens.refreshToken(), 604800))
                .header("Cache-Control", "no-store")
                .build();
    }


    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @APIResponse(
            responseCode = "200",
            description = "Access-токен. Refresh-токен устанавливается в HttpOnly cookie.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AccessTokenResponse.class)
            )
    )
    public Response refresh(@CookieParam("refreshToken") String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ClientErrorException("Refresh-токен отсутствует", 401);
        }

        TokenPairResponse tokens = securityService.refreshTokens(refreshToken);

        return Response.ok(new AccessTokenResponse(tokens.accessToken()))
                .cookie(refreshCookie(tokens.refreshToken(), 604800))
                .header("Cache-Control", "no-store")
                .build();
    }


    @POST
    @Path("/logout")
    @PermitAll
    @APIResponse(
            responseCode = "204",
            description = "Выход выполнен, refresh-cookie удалена"
    )
    public Response logout(@CookieParam("refreshToken") String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            securityService.deleteToken(refreshToken);
        }

        return Response.noContent()
                .cookie(refreshCookie("", 0))
                .build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    public UserResponse me() {
        Long userId = Long.valueOf(jwt.getSubject());
        return securityService.getCurrentUser(userId);
    }

}
