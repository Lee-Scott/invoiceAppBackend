package com.familyfirstsoftware.invoiceApplication.utils;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.familyfirstsoftware.invoiceApplication.domain.HttpResponse;
import com.familyfirstsoftware.invoiceApplication.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.OutputStream;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class ExceptionUtils {


    public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception) {

        if(exception instanceof ApiException ||
                exception instanceof DisabledException ||
                exception instanceof LockedException ||
                exception instanceof BadCredentialsException
        ){
            HttpResponse httpResponse = getHttpResponse(response, exception.getMessage(), BAD_REQUEST);
            writeResponse(httpResponse, response);
        } else if(exception instanceof TokenExpiredException || exception instanceof InvalidClaimException) {
            HttpResponse httpResponse = getHttpResponse(response, "Token has expired. Please login again", UNAUTHORIZED);
            writeResponse(httpResponse, response);
        } else if(exception instanceof AuthenticationException) {
            HttpResponse httpResponse = getHttpResponse(response, "An Authentication Exception occurred. Please try again", INTERNAL_SERVER_ERROR);
            writeResponse(httpResponse, response);
        } else if(exception instanceof ServletException) {
            HttpResponse httpResponse = getHttpResponse(response, "An Servlet Exception occurred. Please try again", INTERNAL_SERVER_ERROR);
            writeResponse(httpResponse, response);
        } else if(exception instanceof IOException ) {
            HttpResponse httpResponse = getHttpResponse(response, "An error occurred. Please try again", INTERNAL_SERVER_ERROR);
            writeResponse(httpResponse, response);
        } else {
            HttpResponse httpResponse = getHttpResponse(response, "An error occurred. Please try again", INTERNAL_SERVER_ERROR);
            writeResponse(httpResponse, response);
        }
        log.error(exception.getMessage());
    }

    private static void writeResponse(HttpResponse httpResponse, HttpServletResponse response) {
        OutputStream out;
        try{
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, httpResponse);
            out.flush();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public static HttpResponse getHttpResponse(HttpServletResponse response, String message, HttpStatus httpStatus) {
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(message)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        return httpResponse;
    }
}
