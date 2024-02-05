package ru.maxbri.springcourse.Project2WithBoot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        //.requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("auth/login", "/error", "/auth/registration").permitAll()
                        .anyRequest().hasAnyRole("USER"))
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login") //адрес, куда нас перекинет чтобы залогиниться
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/books", true)
                        .failureUrl("/auth/login?error")) //это адрес из html формы
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));
        //в идеале еще настроить куки
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
