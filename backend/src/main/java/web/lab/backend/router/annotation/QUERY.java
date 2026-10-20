package web.lab.backend.router.annotation;


import jakarta.ws.rs.HttpMethod;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("QUERY")
@Documented
public @interface QUERY {
}
