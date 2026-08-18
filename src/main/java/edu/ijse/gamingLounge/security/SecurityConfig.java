package edu.ijse.gamingLounge.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/guest-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/customer").permitAll()
                        .requestMatchers("/api/v1/otp/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/branch/**",
                                "/api/v1/station-type/**",
                                "/api/v1/game/**",
                                "/api/v1/snack-category/**",
                                "/api/v1/membership-plan/**",
                                "/api/v1/station/**",
                                "/api/v1/snack/**",
                                "/api/v1/station-game/**",
                                "/api/v1/feedback/average-rating"
                        ).permitAll()
                        .requestMatchers(
                                "/api/v1/employee/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/branch", "/api/v1/station-type",
                                "/api/v1/game", "/api/v1/snack-category", "/api/v1/membership-plan",
                                "/api/v1/station", "/api/v1/snack", "/api/v1/station-game").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/branch", "/api/v1/station-type",
                                "/api/v1/game", "/api/v1/snack-category", "/api/v1/membership-plan",
                                "/api/v1/station", "/api/v1/snack").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/branch/**", "/api/v1/station-type/**", "/api/v1/game/**",
                                "/api/v1/snack-category/**", "/api/v1/membership-plan/**",
                                "/api/v1/station/**", "/api/v1/snack/**", "/api/v1/station-game/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/api/v1/booking/**",
                                "/api/v1/food-order/**",
                                "/api/v1/payment/**",
                                "/api/v1/invoice/**",
                                "/api/v1/membership/**",
                                "/api/v1/feedback/**",
                                "/api/v1/customer/**"
                        ).hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}