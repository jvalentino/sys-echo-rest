package com.github.jvalentino.echo.controller

import com.github.jvalentino.echo.controller.UploadController
import com.github.jvalentino.echo.entity.AuthUser
import com.github.jvalentino.echo.service.DocService
import com.github.jvalentino.echo.service.UserService
import com.github.jvalentino.echo.util.DateGenerator
import com.github.jvalentino.echo.util.DateUtil
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.view.RedirectView
import spock.lang.Specification
import spock.lang.Subject

class UploadControllerTest extends Specification {

    @Subject
    UploadController subject

    def setup() {
        subject = new UploadController()
        subject.with {
            userService = Mock(UserService)
            docService = Mock(DocService)
        }
        GroovyMock(DateGenerator, global:true)
    }

    def "test index"() {
        when:
        String result = subject.index()

        then:
        result == 'upload'
    }

    def "test upload"() {
        given:
        Date date = DateUtil.toDate('2022-10-31T00:00:00.000+0000')
        MultipartFile file = GroovyMock()
        AuthUser user = Mock()

        when:
        RedirectView result = subject.upload(file)

        then:
        1 * DateGenerator.date() >> date
        1 * subject.userService.currentLoggedInUser() >> user
        1 * subject.docService.uploadNewDoc(user, file, date)

        and:
        result.getUrl() == '/upload'
    }

}
