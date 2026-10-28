package web.lab.backend.repository.config;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

@ApplicationScoped
public class JooqProducer {

    @Resource(lookup = "java:jboss/datasources/Lab3DataSource")
    private DataSource dataSource;

    @Produces
    @ApplicationScoped
    public DSLContext produceDslContext() {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }
}
