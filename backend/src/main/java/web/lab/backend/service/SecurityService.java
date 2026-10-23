package web.lab.backend.service;

import com.auth0.jwt.JWT;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import web.lab.backend.dto.auth.*;
import web.lab.backend.dto.response.TokenPairResponse;
import web.lab.backend.dto.response.UserResponse;
import web.lab.backend.repository.RefreshTokenRepository;
import web.lab.backend.repository.UserRepository;
import com.auth0.jwt.algorithms.Algorithm;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;
import java.util.Map;


@ApplicationScoped
public class SecurityService {

    @Inject
    private UserRepository userRepository;
    @Inject
    private RefreshTokenRepository refreshTokenRepository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    private Algorithm jwtAlgorithm;
    private final SecureRandom secureRandom = new SecureRandom();


    @PostConstruct
    void initPasswordHash() {
        String secretBase64Url = System.getenv("JWT_SECRET_BASE64URL");

        passwordHash.initialize(Map.of("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256", "Pbkdf2PasswordHash.Iterations", "600000", "Pbkdf2PasswordHash.SaltSizeBytes", "16", "Pbkdf2PasswordHash.KeySizeBytes", "32"));

        if (secretBase64Url == null || secretBase64Url.isBlank()) {
            throw new IllegalStateException("JWT_SECRET_BASE64URL is not configured");
        }

        byte[] secretBytes = Base64.getUrlDecoder().decode(secretBase64Url);

        if (secretBytes.length < 32) {
            throw new IllegalStateException("JWT secret must contain at least 32 bytes");
        }

        jwtAlgorithm = Algorithm.HMAC256(secretBytes);
    }


    public String generateAccessToken(UserDTO user) {
        Instant now = Instant.now();

        return JWT.create().withIssuer("lab3").withSubject(user.id().toString()).withClaim("upn", user.username()).withArrayClaim("groups", new String[]{user.userRole().name()}).withIssuedAt(Date.from(now)).withExpiresAt(Date.from(now.plusSeconds(900))).sign(jwtAlgorithm);
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashRefreshToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    private String hashPassword(String rawPassword) {
        return passwordHash.generate(rawPassword.toCharArray());
    }

    private boolean passwordMatches(String rawPassword, String storedHash) {
        return passwordHash.verify(rawPassword.toCharArray(), storedHash);
    }


    public UserResponse registerUser(RegisterRequest request) {
        String hashedPassword = hashPassword(request.password());
        UserDTO newUser = userRepository.createUser(request.username(), request.email(), hashedPassword, UserRole.USER);

        return new UserResponse(newUser.id(), newUser.username(), newUser.email(), newUser.userRole());
    }

    public TokenPairResponse loginUser(LoginRequest request) {
        UserDTO user = userRepository.findUserByUsername(request.username()).orElseThrow(() -> new ClientErrorException("Неверный логин или пароль", Response.Status.UNAUTHORIZED));

        if (!passwordMatches(request.password(), user.hashedPassword())) {
            throw new ClientErrorException("Неверный логин или пароль", Response.Status.UNAUTHORIZED);
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken();

        refreshTokenRepository.createRefreshToken(user.id(), hashRefreshToken(refreshToken), OffsetDateTime.now(ZoneOffset.UTC).plusDays(7));

        return new TokenPairResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenPairResponse refreshTokens(RefreshRequest request) {
        String oldTokenHash = hashRefreshToken(request.refreshToken());

        Long userId = refreshTokenRepository.deleteByHash(oldTokenHash).orElseThrow(() -> new ClientErrorException("Refresh-токен недействителен или срок его действия истёк", Response.Status.UNAUTHORIZED));

        UserDTO user = userRepository.findUserById(userId);

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken();

        refreshTokenRepository.createRefreshToken(userId, hashRefreshToken(refreshToken), OffsetDateTime.now(ZoneOffset.UTC).plusDays(7));

        return new TokenPairResponse(accessToken, refreshToken);
    }

    public void deleteToken(RefreshRequest request) {
        String oldTokenHash = hashRefreshToken(request.refreshToken());

        refreshTokenRepository.deleteByHash(oldTokenHash);
    }

}
