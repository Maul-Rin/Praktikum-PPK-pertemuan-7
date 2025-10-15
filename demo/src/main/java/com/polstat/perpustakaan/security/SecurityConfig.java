package com.polstat.perpustakaan.security;

import com.polstat.perpustakaan.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final AuthEntryPoint authEntryPoint;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtFilter jwtFilter, AuthEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.authEntryPoint = authEntryPoint;
    }

    // KOREKSI DARURAT: Menggunakan NoOpPasswordEncoder (PLAINTEXT)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Mendefinisikan DaoAuthenticationProvider secara eksplisit
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint)
                )
                .authorizeHttpRequests(auth -> auth
                        // MODIFIKASI: Tambahkan /docs/** agar Swagger UI/OpenAPI dapat diakses tanpa JWT
                        .requestMatchers("/auth/**", "/docs/**").permitAll()
                        .requestMatchers("/graphql", "/graphiql").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}