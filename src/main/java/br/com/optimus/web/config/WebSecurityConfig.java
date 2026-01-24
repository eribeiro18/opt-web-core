package br.com.optimus.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de Segurança Web para APIs Optimus
 *
 * Esta configuração é compartilhada entre todas as APIs que usam opt-web-core.
 * Fornece:
 * - Autenticação JWT stateless
 * - Liberação automática de Swagger/OpenAPI
 * - Liberação do Actuator
 * - Proteção de todas as demais rotas
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Swagger e OpenAPI - acesso público
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // Actuator - acesso público (pode ser restringido conforme necessidade)
                        .requestMatchers("/actuator/**").permitAll()

                        // Todas as outras rotas exigem autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
