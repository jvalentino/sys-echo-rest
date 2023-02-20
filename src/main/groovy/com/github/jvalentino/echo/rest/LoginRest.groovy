package com.github.jvalentino.echo.rest

import com.github.jvalentino.echo.dto.LoginDto
import com.github.jvalentino.echo.dto.UserDto
import com.github.jvalentino.echo.service.UserService
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpSession

/**
 * Used for handling custom login
 * @author john.valentino
 */
@Controller
@Slf4j
@RestController
@CompileDynamic
class LoginRest {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    UserService userService

    @PostMapping('/custom-login')
    LoginDto login(@RequestBody UserDto user, HttpSession session) {
        LoginDto result = userService.login(user, authenticationManager, session)
        result
    }

}
