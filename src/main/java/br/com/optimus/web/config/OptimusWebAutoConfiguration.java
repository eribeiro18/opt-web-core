package br.com.optimus.web.config;

import br.com.optimus.web.controller.advice.WebControllerAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class OptimusWebAutoConfiguration {

    @Bean
    public WebControllerAdvice webControllerAdvice() {
        return new WebControllerAdvice();
    }
}
