package com.moldi_sams.se_project.configuration;

import com.moldi_sams.se_project.enumeration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // TODO: implement CSRF token after developing the entire API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/hello-world").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/sign-up").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/sign-in").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/authentication/sign-out").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/v1/users/authenticated").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/request-password-reset-code").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/reset-password").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/change-password").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/users-personal-informations/authenticated").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users-personal-informations/authenticated").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/id=**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/product-brands").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-brands/id=**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-brands/name=**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/product-categories").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-categories/id=**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-categories/name=**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/product-genders").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-genders/id=**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-genders/name=**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/product-sizes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-sizes/id=**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-sizes/name=**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/authenticated").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/pending/id=**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/v1/payments/stripe-webhook").permitAll()

                        .anyRequest().hasAnyAuthority(Role.ROLE_ADMINISTRATOR.name())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
