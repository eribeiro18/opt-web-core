package br.com.optimus.web.config;

import br.com.optimus.web.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        Throwable cause = authException.getCause();
        String message = "Credenciais inválidas ou token ausente.";

        if (cause instanceof UnauthorizedException) {
            message = cause.getMessage();
        } else if (authException.getMessage().contains("expired")) {
            message = "Token expirado. Por favor, faça login novamente.";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
                "{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\"}",
                message,
                request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }
}