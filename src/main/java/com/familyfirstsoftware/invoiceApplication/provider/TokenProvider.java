package com.familyfirstsoftware.invoiceApplication.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.familyfirstsoftware.invoiceApplication.domain.UserPrincipal;
import com.familyfirstsoftware.invoiceApplication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
// Todo sub is email on jwt.io for access token but that should be un indemnifying information, like id encrypted
// also login email
@Component
@RequiredArgsConstructor
public class TokenProvider {
    public static final String AUTHORITIES = "authorities";
    private static final String FAMILY_FIRST_SOFTWARE = "FAMILY_FIRST_SOFTWARE";
    private static final String CUSTOMER_MANAGEMENT_SERVICE = "CUSTOMER_MANAGEMENT_SERVICE";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1_800_000;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    @Value("${jwt.secret}")
    private String secret;
    private final UserService userService;

    public String createAccessToken(UserPrincipal userPrincipal) {
        return JWT.create().withIssuer(FAMILY_FIRST_SOFTWARE).withAudience(CUSTOMER_MANAGEMENT_SERVICE)
                .withIssuedAt(new Date()).withSubject(String.valueOf(userPrincipal.getUser().getId())).withArrayClaim(AUTHORITIES, getClaimsFromUser(userPrincipal))
                //.withClaim("email", userPrincipal.getUser().getEmail() // put more into the token
                .withExpiresAt(new Date(currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes()));

    }

    public String createRefreshToken(UserPrincipal userPrincipal) {
        return JWT.create().withIssuer(FAMILY_FIRST_SOFTWARE).withAudience(CUSTOMER_MANAGEMENT_SERVICE)
                .withIssuedAt(new Date()).withSubject(String.valueOf(userPrincipal.getUser().getId()))
                .withExpiresAt(new Date(currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes()));

    }

    public Long getSubject(String token, HttpServletRequest request) {
        try {
            return Long.valueOf(getJWTVerifier().verify(token).getSubject());
        } catch (TokenExpiredException exception) {
            request.setAttribute("expiredMessage", exception.getMessage());
            throw exception;
        } catch (InvalidClaimException exception) {
            request.setAttribute("invalidClaim", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            throw exception;
        }
    }
    /*public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(toList());
    }*/

    public List<GrantedAuthority> getAuthorities(String token) {
        //logger.info(" Received token: {}", token);
        String[] claims = getClaimsFromToken(token);
        //logger.info("Extracted claims: {}", Arrays.toString(claims));

        List<GrantedAuthority> authorities;
        if (claims != null) {
            authorities = stream(claims).map(SimpleGrantedAuthority::new).collect(toList());
        } else {
            // handle the case where claims is null
            // you can return an empty list or throw an exception based on your requirements
            authorities = Collections.emptyList();
        }

        logger.info("Generated authorities: {}", authorities);
        return authorities;
    }

    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new UsernamePasswordAuthenticationToken(userService.getUserById(userId), null, authorities);
        //UsernamePasswordAuthenticationToken userPasswordAuthToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordAuthToken;
    }

    public boolean isTokenValid(Long userId, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return !Objects.isNull(userId) && !isTokenExpired(verifier, token);
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    /*private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }*/

    private String[] getClaimsFromToken(String token) {
        //logger.info("Verifying token: {}", token);
        JWTVerifier verifier = getJWTVerifier();
        String[] claims = verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
        //logger.info("Extracted claims: {}", Arrays.toString(claims));
        return claims;
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(FAMILY_FIRST_SOFTWARE).build();
        }catch (JWTVerificationException exception) { throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED); }
        return verifier;
    }
}
















