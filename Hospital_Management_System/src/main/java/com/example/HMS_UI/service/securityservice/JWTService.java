package com.example.HMS_UI.service.securityservice;

import com.example.HMS_UI.dto.LoginSaveDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    @Autowired
    ApplicationContext context;
    @Value("${jwt.secret}")
    private String secretkey;

    public Authentication getAuthenticatontion(LoginSaveDTO loginSaveDTO) {
        var authenticationManager = context.getBean(AuthenticationManager.class);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginSaveDTO.getEmail(), loginSaveDTO.getPassword()));
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
            return Jwts.builder()
                .addClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100 * 60 * 60*60)) // e.g. 3600000 for 1 hour
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractUserNameFromToken(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey()) // correct for newer versions
                .build()
                .parseClaimsJws(token)
                .getBody(); // note: getBody(), not getPayload()
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName =extractUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}