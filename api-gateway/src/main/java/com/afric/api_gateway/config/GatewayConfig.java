package com.afric.api_gateway.config;

import com.afric.api_gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service API routes
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://auth-service"))

                // Accounting Service API routes
                .route("accounting-service", r -> r
                        .path("/account/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://accounting-service"))

                // Auth Service Swagger UI
                .route("auth-swagger-ui", r -> r
                        .path("/auth-swagger/**")
                        .filters(f -> f.rewritePath("/auth-swagger/(?<segment>.*)", "/swagger-ui/${segment}"))
                        .uri("lb://auth-service"))

                // Auth Service OpenAPI docs
                .route("auth-api-docs", r -> r
                        .path("/auth-api-docs/**")
                        .filters(f -> f.rewritePath("/auth-api-docs/(?<segment>.*)", "/v3/api-docs/${segment}"))
                        .uri("lb://auth-service"))

                // Accounting Service Swagger UI
                .route("accounting-swagger-ui", r -> r
                        .path("/accounting-swagger/**")
                        .filters(f -> f.rewritePath("/accounting-swagger/(?<segment>.*)", "/swagger-ui/${segment}"))
                        .uri("lb://accounting-service"))

                // Accounting Service OpenAPI docs
                .route("accounting-api-docs", r -> r
                        .path("/accounting-api-docs/**")
                        .filters(f -> f.rewritePath("/accounting-api-docs/(?<segment>.*)", "/v3/api-docs/${segment}"))
                        .uri("lb://accounting-service"))

                .build();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Permit public endpoints
                        .pathMatchers("/auth/register", "/auth/login").permitAll()
                        // Permit Swagger UI and OpenAPI docs
                        .pathMatchers("/auth-swagger/**", "/auth-api-docs/**",
                                "/accounting-swagger/**", "/accounting-api-docs/**").permitAll()
                        .anyExchange().authenticated()
                );

        return http.build();
    }
}