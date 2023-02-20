package com.github.jvalentino.echo.controller

import com.github.jvalentino.echo.entity.AuthUser
import com.github.jvalentino.echo.service.DocService
import com.github.jvalentino.echo.service.UserService
import com.github.jvalentino.echo.util.DateGenerator
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.view.RedirectView

/**
 * Used for managing document uploads
 * @author john.valentino
 */
@CompileDynamic
@Controller
@Slf4j
class UploadController {

    @Autowired
    DocService docService

    @Autowired
    UserService userService

    @GetMapping('/upload')
    String index() {
        'upload'
    }

    @PostMapping('/upload-file')
    RedirectView upload(@RequestParam('file') MultipartFile file) {
        AuthUser user = userService.currentLoggedInUser()
        docService.uploadNewDoc(user, file, DateGenerator.date())

        RedirectView redirectView = new RedirectView('/upload', true)
        redirectView
    }

}
