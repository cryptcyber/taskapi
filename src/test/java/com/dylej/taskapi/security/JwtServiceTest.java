package com.dylej.taskapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private static final String SECRET =
            "myVerySecretKeyThatShouldBeMuchLongerForProductionUse123456";

    private final JwtService jwtService = new JwtService(SECRET, 60_000);

    @Test
    void generatesTokenAndExtractsUsername() {
        String token = jwtService.generateToken("test-user");

        assertEquals("test-user", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, "test-user"));
        assertFalse(jwtService.isTokenValid(token, "another-user"));
    }

    @Test
    void rejectsMalformedAndExpiredTokens() {
        assertFalse(jwtService.isTokenValid("not-a-jwt", "test-user"));

        Instant past = Instant.now().minusSeconds(60);
        String expiredToken = Jwts.builder()
                .subject("test-user")
                .issuedAt(Date.from(past))
                .expiration(Date.from(past))
                .signWith(Keys.hmacShaKeyFor(
                        SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertFalse(jwtService.isTokenValid(expiredToken, "test-user"));
    }

    @Test
    void rejectsInvalidConfiguration() {
        assertThrows(IllegalArgumentException.class,
                () -> new JwtService("too-short", 60_000));
        assertThrows(IllegalArgumentException.class,
                () -> new JwtService(SECRET, 0));
        assertThrows(IllegalArgumentException.class,
                () -> jwtService.generateToken(" "));
    }
}
