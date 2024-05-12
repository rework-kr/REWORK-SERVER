package com.example.rework.global;

import com.example.rework.global.error.NotFoundAccountException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class GlobalExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            // Handle exceptions and set the error response
            handleException(ex, response);
        }
    }



    private void handleException(Exception ex, HttpServletResponse response) throws IOException {
        if (ex instanceof NotFoundAccountException) {
            setErrorResponse(response, ErrorCodes.NOT_FOUND_ACCOUNT_EXCEPTION);
        }
        else if (ex instanceof ExpiredJwtException) {
            setErrorResponse(response, ErrorCodes.INVALID_TOKEN_EXCEPTION);
        } else {
            setErrorResponse(response, ErrorCodes.UnAuthorizedException);
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ErrorCodes errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}