package resources.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;



public class JWTGenerator {

    private static final String chaveSecreta = "aoeifh9q83iqh9n3oefciq8u3ef90o18qfuhgf90qi8h90";
    public static String tokenGenerator(String InputRole,String cpf){


        long tempoExpiracao = System.currentTimeMillis() + 3600000 * 3;

        String token = Jwts.builder()
                .setSubject(cpf)
                .claim("role", InputRole)
                .setExpiration(new Date(tempoExpiracao))
                .signWith(key())
                .compact();

        return "Bearer "+token;
    }

    private static Key key() {
        byte[] keyBytes = chaveSecreta.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
