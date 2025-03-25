package be.group16.forum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Development security configuration - allows all requests and disables security mechanisms
     * that could interfere with front-end development
     */
    @Bean
    @Profile("dev")
    public SecurityFilterChain developmentFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF protection for API endpoints
            .csrf(csrf -> csrf.disable())
            
            // Apply CORS configuration
            .cors(cors -> {})
            
            // Disable frame options to allow iframe embedding during development
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
                .xssProtection(xss -> xss.disable())
            )
            
            // Allow all requests without authentication
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            
            // Stateless session management
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        
        return http.build();
    }
    
    /**
     * Production security configuration - currently commented out
     * Uncomment and modify when ready for production deployment
     */
    /*
    @Bean
    @Profile("prod")
    public SecurityFilterChain productionFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())
            
            // Apply CORS configuration
            .cors(cors -> {})
            
            // Configure secure headers 
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                .xssProtection(xss -> xss.enable())
            )
            
            // Specify access rules
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // Remove in production
                
                // Protected endpoints
                .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Default rule
                .anyRequest().authenticated()
            )
            
            // Stateless session management
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
            
            // Add JWT filter here when ready
            // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        return http.build();
    }
    */
    
    /**
     * Configure CORS settings
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://127.0.0.1:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
