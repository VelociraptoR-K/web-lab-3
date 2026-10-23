package web.lab.backend.router;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import web.lab.backend.dto.auth.LoginRequest;
import web.lab.backend.dto.auth.RefreshRequest;
import web.lab.backend.dto.auth.RegisterRequest;
import web.lab.backend.dto.response.TokenPairResponse;
import web.lab.backend.dto.response.UserResponse;
import web.lab.backend.service.SecurityService;

@Path("/auth")
public class SecurityHandler {

    @Inject
    private SecurityService securityService;


    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response register(@NotNull @Valid RegisterRequest request) {
        UserResponse user = securityService.registerUser(request);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public TokenPairResponse login(@NotNull @Valid LoginRequest request) {
        return securityService.loginUser(request);
    }


    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public TokenPairResponse refresh(@NotNull @Valid RefreshRequest request) {
        return securityService.refreshTokens(request);
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response logout(@NotNull @Valid RefreshRequest request) {
        securityService.deleteToken(request);
        return Response.noContent().build();
    }

}
