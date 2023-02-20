package com.github.jvalentino.echo.config

import com.github.jvalentino.echo.service.UserService
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException

/**
 * Another magical class that handles both allowing access of endpoints without auth,
 * and that also established a custom auth manager.
 * @author john.valentino
 */
@Configuration
@EnableWebSecurity
@Slf4j
@CompileDynamic
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService

    // https://stackoverflow.com/questions/4664893/
    // //how-to-manually-set-an-authenticated-user-in-spring-security-springmvc
    @Bean
    AuthenticationManager customAuthenticationManager() throws Exception {
        new AuthenticationManager() {

            @Override
            Authentication authenticate(Authentication authentication) throws AuthenticationException {
                userService.authenticate(authentication)
            }

        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // https://stackoverflow.com/questions/32064000/uploading-file-returns-403-error-spring-mvc
        http.cors().and().csrf().disable()

        http
                .authorizeRequests()
                .antMatchers(
                        '/resources/**',
                        '/webjars/**',
                        '/',
                        '/custom-login',
                        '/invalid'
                ).permitAll()
                .anyRequest().authenticated()
    }

}
