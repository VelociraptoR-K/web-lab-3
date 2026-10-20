package web.lab.backend.middleware;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Set;

@Provider
@PreMatching
public class CorsPreFlightFilter implements ContainerRequestFilter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "http://localhost:2000",
            "http://127.0.0.1:2000"
    );

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (!"OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        String origin = requestContext.getHeaderString("Origin");

        if (origin == null || !ALLOWED_ORIGINS.contains(origin)) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).build()
            );
            return;
        }

        requestContext.abortWith(
                Response.ok()
                        .header("Access-Control-Allow-Origin", origin)
                        .header(
                                "Access-Control-Allow-Methods",
                                "GET, POST, PUT, PATCH, DELETE, QUERY, OPTIONS"
                        )
                        .header(
                                "Access-Control-Allow-Headers",
                                "Content-Type, Authorization"
                        )
                        .build()
        );
    }
}
