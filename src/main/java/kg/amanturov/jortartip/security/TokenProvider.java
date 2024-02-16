package kg.amanturov.jortartip.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;


@Slf4j
@Component
public class TokenProvider {
    public static final String SECRET = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9yJpZCI6IjVjOWYzYWI2NzY2Mjg2NDYyNDY0YTczNCIsIm5hbWUiOiJSYW5keSIsImF2YXRhciI6Ii8vd3d3LmdyYXZhdGFyLmNvbS9hdmF0YXIvMTNhN2MyYzdkOGVkNTNkMDc2MzRkOGNlZWVkZjM0NTEcz0yMDAmcj1wZyZkPW1tIiwiaWF0IjoxNTU0NTIxNjk1LCJleHAiOjE1NTQ1MjUyOTV9SxRurShXSSI3SE11z6nme9EoaD29TDBFr8Qwngkg";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token.replace("{", "").replace("}","")).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*600*1)).signWith(SignatureAlgorithm.HS512,
                        SECRET.getBytes(StandardCharsets.UTF_8)).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
