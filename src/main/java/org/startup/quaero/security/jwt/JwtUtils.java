package org.startup.quaero.security.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.Column;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.startup.quaero.consts.ErrorMessages;
import org.startup.quaero.dto.UserDto;
import org.startup.quaero.enums.Role;
import org.startup.quaero.exceptions.BadTokenException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    private static final String ROLE = "role";

    private static final Integer ACCESS_TOKEN_VALID_TIME_IN_MINUTES = 180;
    private static final Integer REFRESH_TOKEN_VALID_TIME_IN_MINUTES = 1440;

    public static final String ACCESS_TOKEN_KEY = "quaeroIsTheBestThingInMyLifeAndIAmHappyHow";
    public static final String REFRESH_TOKEN_KEY = "quaeroIsTheBestThingInMyLifeAndIAmHappyHow";

    public String createAccessToken(String email, Role role) {
        ClaimsBuilder claims = Jwts.claims().subject(email);
        claims.add(ROLE, Collections.singleton(role.name()));
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, ACCESS_TOKEN_VALID_TIME_IN_MINUTES);
        return Jwts.builder()
                .claims(claims.build())
                .issuedAt(now)
                .expiration(calendar.getTime())
                .signWith(Keys.hmacShaKeyFor(
                        ACCESS_TOKEN_KEY.getBytes(StandardCharsets.UTF_8)),
                        Jwts.SIG.HS256)
                .compact();
    }


    public String createRefreshToken(UserDto user) {
        ClaimsBuilder claims = Jwts.claims().subject(user.getEmail());
        claims.add(ROLE, Collections.singleton(user.getRole().name()));
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, REFRESH_TOKEN_VALID_TIME_IN_MINUTES);
        return Jwts.builder()
                .claims(claims.build())
                .issuedAt(now)
                .expiration(calendar.getTime())
                .signWith(
                        Keys.hmacShaKeyFor(REFRESH_TOKEN_KEY.getBytes(StandardCharsets.UTF_8)),
                        Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String tokenKey) {
        boolean isValid = false;
        SecretKey key = Keys.hmacShaKeyFor(tokenKey.getBytes());
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            isValid = true;
        } catch (Exception e) {
            log.info("Given token is not valid: {}", e.getMessage());
            log.error("Exception:", e);
            log.info("token {}token key {}", token, tokenKey);
        }
        return isValid;
    }

    public String getEmailOutOfAccessToken(String token) {
        String[] splitToken = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(splitToken[1]));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(payload);
        } catch (Exception e) {
            throw new BadTokenException(ErrorMessages.TOKEN_UNABLE_TO_PARSE);
        }
        return jsonNode.path("sub").asText();
    }
}
