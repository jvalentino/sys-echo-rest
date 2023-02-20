package com.github.jvalentino.echo.service

import com.github.jvalentino.echo.model.User
import com.github.jvalentino.echo.util.BaseIntg
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.servlet.view.RedirectView

class UserServiceIntgTest extends BaseIntg {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    UserService userService

    def "test login"() {
        given:
        User user = new User(email:'alpha', password:'bravo')
        userService.saveNewUser('alpha', 'first', 'last', 'bravo')

        when:
        RedirectView result = userService.login(user, authenticationManager)

        then:
        result.getUrl() == '/dashboard'
    }

    def "test login with invalid"() {
        given:
        User user = new User(email:'alpha', password:'bravo')

        when:
        RedirectView result = userService.login(user, authenticationManager)

        then:
        result.getUrl() == '/invalid'
    }
}
