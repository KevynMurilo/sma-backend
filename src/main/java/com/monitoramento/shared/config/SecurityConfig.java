package com.monitoramento.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.security.JwtAuthenticationFilter;
import com.monitoramento.user.domain.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuração de Segurança do Sistema de Monitoramento de Ativos (SMA)
 *
 * Implementa segurança baseada em Roles (RBAC - Role-Based Access Control)
 * usando JWT (JSON Web Tokens) para autenticação stateless.
 *
 * ROLES DISPONÍVEIS:
 * - ROLE_ADMIN:     Acesso total ao sistema
 * - ROLE_MANAGER:   Acesso restrito aos seus departamentos
 * - ROLE_DRIVER:    Acesso às suas viagens e operações de motorista
 * - ROLE_PASSENGER: Acesso público + favoritos pessoais
 * - ROLE_SYSTEM:    Acesso para dispositivos IoT (ingestão de dados)
 *
 * @author SMA Team
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            ApiResponseDTO.error(401, "Não autorizado: " + authException.getMessage())
                    )
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint()))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/public/**").permitAll()

                        // ====================================
                        // ROLE_SYSTEM - Dispositivos IoT
                        // ====================================
                        // Apenas ingestão de dados de GPS
                        .requestMatchers(HttpMethod.POST, "/api/v1/tracking/ingest").hasRole("SYSTEM")

                        // ====================================
                        // ROLE_MANAGER - Gerentes de Frota
                        // ====================================
                        // Acesso restrito aos seus departamentos
                        .requestMatchers("/api/v1/manager/**").hasRole("MANAGER")

                        // ====================================
                        // ROLE_DRIVER - Motoristas
                        // ====================================
                        // Gerenciar suas próprias viagens
                        .requestMatchers("/api/v1/driver/**").hasRole("DRIVER")

                        // ====================================
                        // AUTENTICADO - Qualquer usuário logado
                        // ====================================
                        // Gerenciar favoritos (rotas favoritas)
                        .requestMatchers("/api/v1/favorites/**").authenticated()

                        // ====================================
                        // ROLE_ADMIN - Administradores
                        // ====================================
                        // Acesso total ao sistema
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // ====================================
                        // PADRÃO - Requer autenticação
                        // ====================================
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}