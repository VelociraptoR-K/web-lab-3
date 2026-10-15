package web.lab.backend.middleware;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.*;
import  java.util.Set;



@Provider
public class CorsFilter implements ContainerResponseFilter, ContainerRequestFilter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of("http://127.0.0.1:2000", "http://localhost:2000");

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            String origin = requestContext.getHeaderString("Origin");
            String isAllowed = (origin != null && ALLOWED_ORIGINS.contains(origin)) ? origin : "null";

            Response optionPrefilghtResponse = Response.ok()
                    .header("Access-Control-Allow-Origin", isAllowed)
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                    .build();
            requestContext.abortWith(optionPrefilghtResponse);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String origin = requestContext.getHeaderString("Origin");
        String isAllowed = (origin != null && ALLOWED_ORIGINS.contains(origin)) ? origin : "null";

        responseContext.getHeaders().add("Access-Control-Allow-Origin", isAllowed);
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, QUERY, OPTIONS");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
