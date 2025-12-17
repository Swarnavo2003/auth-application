package in.swarnavo.auth_backend.config;

import in.swarnavo.auth_backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorizeHttpRequest ->
                authorizeHttpRequest
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .anyRequest().authenticated()
        ).exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, e) -> {
            e.printStackTrace();
            response.setStatus(401);
            response.setContentType("application/json");
            String message = "Unauthorized Access : " + e.getMessage();
            Map<String, String> errorMap = Map.of(
                    "message", message,
                    "status", String.valueOf(HttpStatus.UNAUTHORIZED),
                    "statusCode", String.valueOf(401)
            );
            var objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(errorMap));
        })).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService users() {
//        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
//        UserDetails user1 = userBuilder.username("ankit").password("abc").roles("ADMIN").build();
//        UserDetails user2 = userBuilder.username("shiva").password("xyz").roles("ADMIN").build();
//        UserDetails user3 = userBuilder.username("durgesh").password("pqr").roles("USER").build();
//        return new InMemoryUserDetailsManager(user1, user2, user3);
//    }
}
