package web.lab.backend.middleware;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.*;
import  java.util.Set;



@Provider

public class CorsResponseFilter implements ContainerResponseFilter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of("http://127.0.0.1:2000", "http://localhost:2000");

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String origin = requestContext.getHeaderString("Origin");

        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
            responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, QUERY, OPTIONS");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Authorization");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Vary", "Origin");
        }
    }
}
