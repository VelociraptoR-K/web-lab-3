package web.lab.backend.repository;


import static jooq.generated.tables.Users.USERS;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import web.lab.backend.dto.auth.UserDTO;
import web.lab.backend.dto.auth.UserRole;

import javax.sql.DataSource;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {

    @Inject
    private DSLContext dsl;


    public UserDTO createUser(String username, String email, String hashedPassword, UserRole role) {
        try {
            var newUserId = dsl.insertInto(USERS,
                            USERS.USERNAME,
                            USERS.EMAIL,
                            USERS.PASSWORD_HASH,
                            USERS.ROLE)
                    .values(
                            username,
                            email,
                            hashedPassword,
                            jooq.generated.enums.UserRole.valueOf(role.name()))
                    .returning(
                            USERS.ID)
                    .fetchOne();

            return new UserDTO(
                    newUserId.getId(),
                    username,
                    email,
                    hashedPassword,
                    role
            );
        } catch (DataAccessException exception) {
            if ("23505".equals(exception.sqlState())) {
                throw new ClientErrorException(
                        "User with this Username or Email already exists",
                        Response.Status.CONFLICT
                );
            }
            throw exception;
        }
    }

    public UserDTO findUserById(Long id) {
        UserDTO user = dsl.select(
                        USERS.ID,
                        USERS.USERNAME,
                        USERS.EMAIL,
                        USERS.PASSWORD_HASH,
                        USERS.ROLE
                )
                .from(USERS)
                .where(USERS.ID.eq(id))
                .fetchOne(record -> new UserDTO(
                        record.get(USERS.ID),
                        record.get(USERS.USERNAME),
                        record.get(USERS.EMAIL),
                        record.get(USERS.PASSWORD_HASH),
                        UserRole.valueOf(record.get(USERS.ROLE).getLiteral())
                ));

        if (user == null) {
            throw new NotFoundException("User with id=" + id + " was not found");
        }

        return user;
    }

    public Optional<UserDTO> findUserByUsername(String username) {
        UserDTO user = dsl.select(
                        USERS.ID,
                        USERS.USERNAME,
                        USERS.EMAIL,
                        USERS.PASSWORD_HASH,
                        USERS.ROLE
                )
                .from(USERS)
                .where(USERS.USERNAME.eq(username))
                .fetchOne(record -> new UserDTO(
                        record.get(USERS.ID),
                        record.get(USERS.USERNAME),
                        record.get(USERS.EMAIL),
                        record.get(USERS.PASSWORD_HASH),
                        UserRole.valueOf(record.get(USERS.ROLE).getLiteral())
                ));

        return Optional.ofNullable(user);
    }

}
