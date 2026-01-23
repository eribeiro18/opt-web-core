package br.com.optimus.web.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        String jsonResponse = String.format(
                "{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"Acesso negado. O usuario nao possui a permissao necessaria para este recurso (403).\", \"path\": \"%s\", \"Action\": \"%s\"}",
                request.getRequestURI(), validateAction(request)
        );

        response.getWriter().write(jsonResponse);
    }

    private String validateAction(HttpServletRequest request){
        return switch (request.getMethod()) {
            case "GET" -> "READ";
            case "POST" -> "CREATE";
            case "PUT" -> "UPDATE";
            case "PATCH" -> "UPDATE/DELETE";
            case "DELETE" -> "DELETE";
            default -> "Invalid method";
        };
    }
}