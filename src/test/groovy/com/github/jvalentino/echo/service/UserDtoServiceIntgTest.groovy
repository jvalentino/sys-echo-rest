package com.github.jvalentino.echo.service

import com.github.jvalentino.echo.dto.ResultDto
import com.github.jvalentino.echo.dto.UserDto
import com.github.jvalentino.echo.util.BaseIntg
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.servlet.view.RedirectView

import javax.servlet.http.HttpSession

class UserDtoServiceIntgTest extends BaseIntg {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    UserService userService

    def "test login"() {
        given:
        UserDto user = new UserDto(email:'alpha', password:'bravo')
        userService.saveNewUser('alpha', 'first', 'last', 'bravo')
        HttpSession session = GroovyMock()

        when:
        ResultDto result = userService.login(user, authenticationManager, session)

        then:
        1 * session.getId() >> 'abc'
        result.success
    }

    def "test login with invalid"() {
        given:
        UserDto user = new UserDto(email:'alpha', password:'bravo')
        HttpSession session = GroovyMock()

        when:
        ResultDto result = userService.login(user, authenticationManager, session)

        then:
        result.success == false
    }
}
