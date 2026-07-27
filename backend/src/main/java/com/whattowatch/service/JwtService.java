package com.whattowatch.service;

import com.whattowatch.config.AppProps;
import com.whattowatch.exception.AppConfigurationException;
import com.whattowatch.exception.InvalidCredentialsException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.time.Clock;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    private static final int MIN_KEY_LENGTH_BYTES = 32;
    
    private final Clock clock;
    private final long expirationMs;
    private final SecretKey signingKey;
    private final JwtParser jwtParser;

    @Autowired
    public JwtService(AppProps props) {
        this(props, Clock.systemUTC());
    }

    public JwtService(AppProps props, Clock clock) {
        Objects.requireNonNull(props, "AppProps ne doit pas être null");
        this.clock = Objects.requireNonNull(clock, "Clock ne doit pas être null");


        this.expirationMs = props.getJwt().getExpirationMs();
        this.signingKey = createSigningKey(props.getJwt().getSecret());

        this.jwtParser = Jwts.parser().verifyWith(signingKey).clock(() -> new Date(this.clock.millis())).build();
    }

    private SecretKey createSigningKey(String encodedSecret) {
        if (encodedSecret == null || encodedSecret.isBlank()) {
            throw new AppConfigurationException("app.jwt.secret est obligatoire");
        }

        try {
            byte[] keyBytes = Decoders.BASE64.decode(encodedSecret);

            if (keyBytes.length < MIN_KEY_LENGTH_BYTES) {
                throw new AppConfigurationException("app.jwt.secret doit être une clé Base64 d'au moins 256 bits (32 octets).");
            }

            return Keys.hmacShaKeyFor(keyBytes);
        }
        catch (IllegalArgumentException ex) {
            throw new AppConfigurationException("app.jwt.secret doit être une clé Base64 valide.");
        }
    }

    public String generateToken(String subjectEmail) {
        if (subjectEmail == null || subjectEmail.isBlank()) {
            throw new IllegalArgumentException("l'email du token est obligatoire");
        }

        long now = clock.millis();
        long expiration;

        try {
            expiration= Math.addExact(now, expirationMs);
        }
        catch (ArithmeticException ex) {
            throw new AppConfigurationException("app.jwt.expiration-ms est trop élevé.");
        }

        return Jwts.builder()
                .subject(subjectEmail)
                .issuedAt(new Date(now))
                .expiration(new Date(expiration))
                .signWith(signingKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidCredentialsException("JWT absent ou invalide");
        }

        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        }
        catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidCredentialsException("JWT invalide ou expiré");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Objects.requireNonNull(claimsResolver, "claimsResolver ne doit pas être null");

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.getSubject());
    }
}