package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

        // Leer origenes permitidos desde variables de entorno (opcional)
        @Value("${cors.allowed.origins:http://localhost:4200}")
        private String allowedOrigins;

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // CONFIGURACIÓN ACTUAL:
                // Por ahora solo permite localhost:4200 para desarrollo local
                //
                // CUANDO DESPLIEGUES EL FRONTEND:
                // Opción 1: Agrega más URLs aquí directamente
                // Opción 2: Configura la variable de entorno CORS_ALLOWED_ORIGINS en Render
                //
                // Ejemplos de dominios del frontend:
                // - http://localhost:4200 (desarrollo local) ✅ YA CONFIGURADO
                // - https://restobar-frontend.vercel.app (si usas Vercel)
                // - https://restobar-frontend.onrender.com (si usas Render)
                // - https://tu-dominio-personalizado.com (si tienes dominio propio)

                configuration.setAllowedOrigins(Arrays.asList(
                                "http://localhost:4200", // ✅ Para desarrollo local
                                "http://localhost:4201", // ✅ Por si usas otro puerto
                                "https://localhost:4200" // ✅ HTTPS local
                // Agrega aquí el dominio de tu frontend cuando lo despliegues:
                // "https://restobar-frontend.vercel.app",
                // "https://restobar-frontend.onrender.com",
                ));

                // Permitir credenciales (cookies, headers de autorización)
                configuration.setAllowCredentials(true);

                // Permitir todos los headers comunes
                configuration.setAllowedHeaders(List.of(
                                "Authorization",
                                "Content-Type",
                                "Accept",
                                "Origin",
                                "X-Requested-With",
                                "Access-Control-Request-Method",
                                "Access-Control-Request-Headers"));

                // Métodos HTTP permitidos
                configuration.setAllowedMethods(Arrays.asList(
                                "GET",
                                "POST",
                                "PUT",
                                "DELETE",
                                "OPTIONS",
                                "PATCH"));

                // Headers que el cliente puede leer
                configuration.setExposedHeaders(List.of(
                                "Authorization",
                                "Content-Disposition"));

                // Tiempo de caché para la respuesta preflight (OPTIONS) en segundos
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}
