package com.example.gateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;

@RestController
public class MockMicroservicesController {

    private final String SECRET_STRING = "3cfa76ef8a9b6c4d5e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d";

        @PostMapping("/api/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        if ("alex".equals(request.username()) && "password123".equals(request.password())) {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
            
            // Construir el token especificando de forma explícita el algoritmo HS256 en la firma
            String jwt = Jwts.builder()
                    .header()
                        .type("JWT")
                        .and()
                    .subject(request.username())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 3600000))
                    .signWith(key, Jwts.SIG.HS256) // Firma robusta estándar exigida por Spring Boot
                    .compact();
                    
            return ResponseEntity.ok(Map.of("token", jwt));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

      @GetMapping("/api/orders")
    public ResponseEntity<List<Map<String, Object>>> getInternalOrders() {
        Map<String, Object> order1 = Map.<String, Object>of("id", 101, "item", "Laptop Dell", "price", 1200.00);
        Map<String, Object> order2 = Map.<String, Object>of("id", 102, "item", "Mouse Ergonomico", "price", 45.00);
        
        return ResponseEntity.ok(List.of(order1, order2));
    }


}
record LoginRequest(String username, String password) {}
