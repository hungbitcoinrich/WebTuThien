package com.example.WebBanSach.security;

import com.example.WebBanSach.services.CustomOAuth2UserService;
import com.example.WebBanSach.services.CustomUserDetailServices;
import com.example.WebBanSach.services.UserServices;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private UserServices userServices;
    @Autowired
    public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailServices();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/register", "/error", "/fonts/**", "/Customer/**","/reset-password","/change-password","/users","/status","/policy","/orderingmethod","/shippingmethod","/paymentmethod","/userguide")
                        .permitAll()
                        .requestMatchers("/Admin/img/**").permitAll()
                        .requestMatchers("/Admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/books/edit", "/books/delete", "/books", "/books/add","/css/**", "/js/**", "/images/**", "/", "/register", "/error", "/fonts/**", "/Customer/**","/reset-password","/change-password","/cart/**")
                        .hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2.loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                )
                .rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                        .userDetailsService(userDetailsService())
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedPage("/403"))
                .build();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }



    @PostConstruct
    public void init() {
        userServices.createAdminUser();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            if (exception.getMessage().contains("Tài khoản của bạn đã bị khóa")) {
                response.sendRedirect("/login?error=locked");
            } else {
                response.sendRedirect("/login?error");
            }
        };
    }
}
