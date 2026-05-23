package es.ieslavereda.plateup.security;

import es.ieslavereda.plateup.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    // Clave secreta para firmar y verificar los tokens, se lee desde la configuración
    @Value("${app.jwt.secret}")
    private String secret;

    // Tiempo de validez del token en milisegundos (configurado en application.properties)
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    // Genera un token JWT con los datos básicos del usuario: nombre, id, email y rol
    public String generateToken(User user) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(getSigningKey())
                .compact();
    }

    // Extrae el nombre de usuario almacenado como subject dentro del token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Comprueba que el token pertenece al usuario indicado y que todavía no ha caducado
    public boolean isTokenValid(String token, User user) {
        String username = extractUsername(token);
        return username != null
                && username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    // Devuelve true si la fecha de expiración del token ya ha pasado
    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // Parsea y verifica la firma del token para obtener todos sus claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Construye la clave de firma a partir del secreto configurado usando HMAC-SHA
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}