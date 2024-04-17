package com.app.countrydata.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.countrydata.Security.JwtAuthenticationEntryPoint;
import com.app.countrydata.Security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;
    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection (allows testing of login / logout)
        // http.csrf(csrf -> csrf.disable())
        //         .authorizeRequests(requests -> requests.requestMatchers("/auth/login").permitAll()
        //                 .anyRequest()
        //                 .authenticated()).exceptionHandling(ex -> ex.authenticationEntryPoint(point))
        //         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        // return http.build();
            // Disable CSRF protection (allows testing of login / logout)
    http.csrf(csrf -> csrf.disable())
    .authorizeRequests(requests -> requests.anyRequest().permitAll())
    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
 return http.build();
    }


}