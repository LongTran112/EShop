package com.shopme.admin.security;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final String rememberMeKey;

    public WebSecurityConfig(@Value("${security.rememberme.key:}") String configuredRememberMeKey) {
        this.rememberMeKey = resolveRememberMeKey(configuredRememberMeKey);
    }

    private String resolveRememberMeKey(String configuredRememberMeKey) {
        if (configuredRememberMeKey != null && !configuredRememberMeKey.trim().isEmpty()) {
            return configuredRememberMeKey.trim();
        }

        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
                .antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
                .antMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")

                .antMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")

                .antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
                .hasAnyAuthority("Admin", "Editor", "Salesperson")

                .antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")

                .antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")

                .antMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")

                .antMatchers("/customers/**", "/orders/**", "/get_shipping_cost", "/reports/**").hasAnyAuthority("Admin", "Salesperson")

                .antMatchers("/orders_shipper/update/**").hasAuthority("Shipper")

                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .and().logout().permitAll()
                .and()
                .rememberMe().userDetailsService(userDetailsService())
                .key(rememberMeKey)
                .tokenValiditySeconds(14 * 24 * 60 * 60);



        http.headers().frameOptions().sameOrigin();
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }





    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**",
                "/js/**",
                "/webjars/**",
                "/styles/**",
                "/error");
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
