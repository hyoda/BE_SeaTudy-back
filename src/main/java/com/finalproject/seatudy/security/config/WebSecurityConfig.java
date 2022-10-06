package com.finalproject.seatudy.security.config;

import com.finalproject.seatudy.security.FilterSkipMatcher;
import com.finalproject.seatudy.security.exception.AuthenticationFailHandler;
import com.finalproject.seatudy.security.filter.JwtAuthFilter;
import com.finalproject.seatudy.security.jwt.HeaderTokenExtractor;
import com.finalproject.seatudy.security.provider.JWTAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JWTAuthProvider jwtAuthProvider;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthenticationFailHandler authenticationFailHandler;


    @Value("${front.base.url}")
    private String FRONT_BASE_URL;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    //swagger
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .antMatchers( //Swagger 문서 읽기위한 요청허용
                        "/swagger-ui.html/**",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/swagger/**", "/h2-console/**", "/swagger-ui.html",
                        "/api/v1/chat/connections/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthProvider);

        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable();
        http.httpBasic().disable()
                .authorizeRequests()

                .antMatchers(HttpMethod.OPTIONS).permitAll(); // preflight 대응

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")
                .permitAll();

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    private JwtAuthFilter jwtFilter() throws Exception {
        List<String> skipPathList = new ArrayList<>();

        skipPathList.add("GET,/images/**");
        skipPathList.add("GET,/css/**");

        skipPathList.add("GET,/api/v1/members/kakaoLogin/**");
        skipPathList.add("GET,/api/v1/members/naverLogin/**");
        skipPathList.add("GET,/api/v1/members/googleLogin/**");

        skipPathList.add("POST,/api/member/signup");
        skipPathList.add("POST,/api/member/login/**");

        skipPathList.add("GET,/basic.js");
        skipPathList.add("GET,/favicon.ico");

        FilterSkipMatcher matcher = new FilterSkipMatcher(skipPathList, "/**");
        JwtAuthFilter filter = new JwtAuthFilter(matcher,headerTokenExtractor);

        filter.setAuthenticationFailureHandler(authenticationFailHandler);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(FRONT_BASE_URL);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}