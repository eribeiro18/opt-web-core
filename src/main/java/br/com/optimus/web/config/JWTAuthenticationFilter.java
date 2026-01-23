package br.com.optimus.web.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

	private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		String path = httpReq.getRequestURI();
		try {
			Authentication authentication = JwtTokenProvider.getAuthentication(httpReq);
			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("Autenticacao JWT ok para: {}", authentication.getName());
			} else {
				log.debug("Sem token JWT na requisição para {}", path);
			}
		} catch (Exception e) {
			log.debug("JWT validation failed: {}", e.getMessage());
			SecurityContextHolder.clearContext();
		}
		chain.doFilter(request, response);
	}
}
