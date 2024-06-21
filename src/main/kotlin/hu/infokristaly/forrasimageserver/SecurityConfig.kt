package hu.infokristaly.forrasimageserver

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests{request-> request
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .anyRequest().authenticated()}
        http.sessionManagement{session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
        http.httpBasic(Customizer.withDefaults())
        http.csrf { csrf -> csrf.disable() }
        return http.build()
    }
}