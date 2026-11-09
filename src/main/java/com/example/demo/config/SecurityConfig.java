package com.example.demo.config;

import com.example.demo.filter.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthorizationFilter jwtAuthorizationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter, CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (cualquiera puede verlos)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/estadios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/eventos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/zonas/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/asientos/**").permitAll()

                        // Endpoints solo para ADMIN
                        .requestMatchers(HttpMethod.POST, "/estadios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/estadios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/estadios/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/eventos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/eventos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/eventos/**").hasRole("ADMIN")

                        // Cualquier otra petición requiere estar autenticado
                        .anyRequest().authenticated()
                )
                // Agregar el filtro JWT ANTES del filtro de autenticación por defecto
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // Exponer el AuthenticationManager como Bean para usarlo en AuthController
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}