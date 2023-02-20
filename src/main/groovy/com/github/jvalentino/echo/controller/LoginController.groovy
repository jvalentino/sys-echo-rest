package com.github.jvalentino.echo.controller

import com.github.jvalentino.echo.model.User
import com.github.jvalentino.echo.service.UserService
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.view.RedirectView

/**
 * Used for handling custom login
 * @author john.valentino
 */
@Controller
@Slf4j
@CompileDynamic
class LoginController {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    UserService userService

    // https://stackoverflow.com/questions/29794096/spring-security-authentication-authorization-via-rest-endpoint
    // https://hellokoding.com/registration-and-login-example-with-spring-security-spring-boot-spring-data-jpa-hsql-jsp/
    @PostMapping('/custom-login')
    RedirectView login(@ModelAttribute('user') User user) {
        userService.login(user, authenticationManager)
    }

}
