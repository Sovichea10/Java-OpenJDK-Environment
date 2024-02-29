package com.camcyber.config;

import com.camcyber.shares.exception.CustomAccessDeniedHandler;
import com.camcyber.shares.exception.CustomAuthenticationEntryPoint;
import com.camcyber.shares.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] WHITE_LIST_URL = {"/api/auth/**","/api/files/storage/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserInfoUserDetailService();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .requestMatchers("/api/auth/**","/api/files/storage/**",
//                        "/v2/api-docs",
//                        "/v3/api-docs",
//                        "/v3/api-docs/**",
//                        "/swagger-resources",
//                        "/swagger-resources/**",
//                        "/configuration/ui",
//                        "/configuration/security",
//                        "/swagger-ui/**",
//                        "/webjars/**",
//                        "/swagger-ui.html").permitAll()
//                .antMatchers(
//                        "/api/user/**", "/api/product/**", "/api/dashboard/**",
//                        "/api/order/**", "/api/product-type/**", "/api/user/**", "/api/user-type/**"
//                ).authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req ->
                    req.requestMatchers(WHITE_LIST_URL)
                            .permitAll()
                            .anyRequest()
                            .authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandling ->
                    exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                            .accessDeniedHandler(new CustomAccessDeniedHandler())
            )
    // Uncomment the following lines if you want to handle logout as well
//        .logout(logout ->
//                logout.logoutUrl("/api/v1/auth/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//        )
    ;

    return http.build();
}




    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(HttpStatus.FORBIDDEN);
            errorResponse.setCode(HttpStatus.FORBIDDEN.getReasonPhrase());
            errorResponse.setDate(String.valueOf(LocalDate.now()));
            response.getWriter().write("Forbidden");};}

}
