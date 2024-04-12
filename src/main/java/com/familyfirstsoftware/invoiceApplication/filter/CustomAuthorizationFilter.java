package com.familyfirstsoftware.invoiceApplication.filter;


import com.familyfirstsoftware.invoiceApplication.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.familyfirstsoftware.invoiceApplication.constant.Constants.*;
import static com.familyfirstsoftware.invoiceApplication.utils.ExceptionUtils.processError;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        try {
            //Map<String, String> values = getRequestValues(request); // using long now
            String token = getToken(request);
            long userId = getUserId(request);
            //System.out.println("TOKEN: " + token);
            if (tokenProvider.isTokenValid(userId, token)) {
                List<GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
                // set authentication
                Authentication authentication = tokenProvider.getAuthentication(userId, authorities, request);
                //System.out.println("*** AUTHENTICATION in CustomAuthorizationFilter.doFilterInternal: " + authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
            // filters are going in a loop this just places the filter in the chain
            filter.doFilter(request, response);
        } catch (Exception exception) {
            log.error("doFilterInternal: " + exception.getMessage());
            processError(request, response, exception);
        }

    }

    // called first
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //log.info("shouldNotFilter: " + request.getRequestURI());
        return request.getHeader(AUTHORIZATION) == null || !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) ||
                request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }

    private Long getUserId(HttpServletRequest request) {
        return tokenProvider.getSubject(getToken(request), request);
    }

    private String getToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(token -> token.replace(TOKEN_PREFIX, EMPTY)).get();
    }
}