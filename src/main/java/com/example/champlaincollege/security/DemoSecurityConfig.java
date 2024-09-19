package com.example.champlaincollege.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class DemoSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails john = User.withUsername("john")
                .password("{noop}test123") // Using Noop for plaintext password
                .roles("employee")
                .build();

        UserDetails mary = User.withUsername("annie")
                .password("{noop}test123") // Example password
                .roles("manager","employee")
                .build();

        UserDetails susan = User.withUsername("vicky")
                .password("{noop}test123") // Example password
                .roles("admin","employee","manager")
                .build();

        return new InMemoryUserDetailsManager(john, mary, susan);
    }

    @Bean

public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").hasRole("employee")
                                .requestMatchers("/leaders/**").hasRole("manager")
                                .requestMatchers("/systems/**").hasRole("admin")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl(("/authenticateTheUser"))
                                .permitAll()

                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/access-denied")
                );


        return http.build();



    }
}
