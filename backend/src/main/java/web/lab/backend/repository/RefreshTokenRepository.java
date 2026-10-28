package web.lab.backend.repository;

import static jooq.generated.tables.RefreshTokens.REFRESH_TOKENS;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.jooq.DSLContext;
import org.jooq.Records;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import web.lab.backend.dto.auth.RefreshTokenDTO;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


@ApplicationScoped
public class RefreshTokenRepository {

    @Inject
    private DSLContext dsl;

    public RefreshTokenDTO createRefreshToken(Long userId, String hashedToken, OffsetDateTime expiresAt) {
        var newRefreshToken = dsl.insertInto(REFRESH_TOKENS,
                        REFRESH_TOKENS.USER_ID,
                        REFRESH_TOKENS.TOKEN_HASH,
                        REFRESH_TOKENS.EXPIRES_AT)
                .values(
                        userId,
                        hashedToken,
                        expiresAt)
                .returning(
                        REFRESH_TOKENS.ID)
                .fetchOne();

        return new RefreshTokenDTO(
                newRefreshToken.getId(),
                userId,
                hashedToken,
                expiresAt);

    }

    public Optional<RefreshTokenDTO> findByHash(String hashedToken) {
        RefreshTokenDTO hash = dsl.select(
                        REFRESH_TOKENS.ID,
                        REFRESH_TOKENS.USER_ID,
                        REFRESH_TOKENS.TOKEN_HASH,
                        REFRESH_TOKENS.EXPIRES_AT
                )
                .from(REFRESH_TOKENS)
                .where(REFRESH_TOKENS.TOKEN_HASH.eq(hashedToken))
                .fetchOne(Records.mapping(RefreshTokenDTO::new));

        return Optional.ofNullable(hash);
    }

    public Optional<Long> deleteByHash(String hashedToken) {
        var deleted = dsl.deleteFrom(REFRESH_TOKENS)
                .where(REFRESH_TOKENS.TOKEN_HASH.eq(hashedToken))
                .and(REFRESH_TOKENS.EXPIRES_AT.gt(OffsetDateTime.now(ZoneOffset.UTC)))
                .returning(REFRESH_TOKENS.USER_ID)
                .fetchOne();

        return deleted == null
                ? Optional.empty()
                : Optional.of(deleted.getUserId());
    }


}
