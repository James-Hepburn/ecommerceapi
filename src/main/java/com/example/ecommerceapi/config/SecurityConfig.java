package com.example.ecommerceapi.config;

import com.example.ecommerceapi.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private CustomUserDetailsService userDetailsService;
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf (csrf -> csrf.disable ())
                .authorizeHttpRequests (auth -> auth
                        .requestMatchers ("/auth/**").permitAll ()
                        .anyRequest ().authenticated ()
                )
                .sessionManagement (sess -> sess.sessionCreationPolicy (SessionCreationPolicy.STATELESS));

        http.authenticationProvider (authProvider ());
        http.addFilterBefore (this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build ();
    }

    @Bean
    public DaoAuthenticationProvider authProvider () {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider ();
        authProvider.setUserDetailsService (this.userDetailsService);
        authProvider.setPasswordEncoder (passwordEncoder ());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder ();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager ();
    }
}
