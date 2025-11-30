package com.example.demo.config;

import com.example.demo.Security.JWTFilter;
import com.example.demo.Security.JWTUtil;
import com.example.demo.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JWTUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    // Configuración del SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {
                }) // Deshabilitamos CSRF ya que no lo necesitamos con JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/registro").permitAll()
                        .requestMatchers("/api/promociones/public/**").permitAll()
                        .requestMatchers("/api/productos/public").permitAll()
                        .requestMatchers("/api/pagos/procesar").permitAll()
                        .requestMatchers("/api/reservas/public/**").permitAll()
                        .requestMatchers("/api/mesas/public/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTFilter(jwtUtil, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class); // Agregar el filtro JWT antes de la autenticación

        return http.build(); // Construir la configuración de seguridad
    }

    // Bean para AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Se configura el AuthenticationManager sin usar 'build()' de forma explícita
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService) // Configuramos el UserDetailsService
                .passwordEncoder(passwordEncoder()); // Configuramos el PasswordEncoder

        return authenticationManagerBuilder.build(); // Usamos 'build()' ahora correctamente aquí
    }

    // Bean para PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
