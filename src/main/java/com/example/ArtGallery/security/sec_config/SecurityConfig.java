package com.example.ArtGallery.security.sec_config;

import com.example.ArtGallery.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private TokenFilter filter;

    public SecurityConfig(TokenFilter filter) {
        this.filter = filter;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x -> x
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x -> x
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                                .requestMatchers(HttpMethod.POST, "/register").permitAll() // Разрешить доступ к регистрации
                                .requestMatchers(HttpMethod.GET, "/activate").permitAll() // Разрешить доступ к активации
                                .requestMatchers(HttpMethod.PUT, "/users/{id}/updateFields").hasAnyAuthority("ADMIN", "ARTIST")
                                .requestMatchers(HttpMethod.POST, "/works").hasAnyAuthority("ADMIN", "ARTIST")
                                .requestMatchers(HttpMethod.PUT, "/users").hasAnyAuthority("ADMIN", "ARTIST")
                                .requestMatchers(HttpMethod.GET, "/works").permitAll()
                                .requestMatchers(HttpMethod.GET, "/works").permitAll()
                                .requestMatchers(HttpMethod.GET, "/works/byCategory/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/users/{id}/self").hasAnyAuthority("ADMIN", "ARTIST")
                                .requestMatchers(HttpMethod.GET, "/works/byCategory/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/artists").permitAll()
                                .requestMatchers(HttpMethod.GET, "/categories").permitAll()
//                                .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
