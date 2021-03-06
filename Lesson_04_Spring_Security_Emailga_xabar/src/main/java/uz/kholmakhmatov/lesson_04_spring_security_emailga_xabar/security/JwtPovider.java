package uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtPovider {
    private static final long expireTime = 1000 * 60 * 60 * 24;
    private static final String secretKey = "Anvar";

    public String generateToken(String username, Set<Role> roles) {
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return token;
    }

    public String getEmailFromToken(String token) {
        try {
            String username = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return username;

        } catch (Exception e) {
            return null;
        }

    }
}
