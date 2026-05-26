package br.com.fiap.esg.infra.security;

import br.com.fiap.esg.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final Algorithm algoritmo;
    private final JWTVerifier verifier;

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        validarSecret(secret);
        this.algoritmo = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algoritmo)
                .withIssuer("ESG API")
                .build();
    }

    public String gerarToken(Usuario usuario) {
        try {
            return JWT.create()
                    .withIssuer("ESG API")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            return verifier.verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new TokenInvalidoException("Token JWT invalido ou expirado!");
        }
    }

    private void validarSecret(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("A variavel API_SECURITY_TOKEN_SECRET deve ser informada.");
        }

        if (secret.length() < 8) {
            throw new IllegalStateException("A variavel API_SECURITY_TOKEN_SECRET deve ter pelo menos 8 caracteres.");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
