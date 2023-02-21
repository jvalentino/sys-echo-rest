package com.github.jvalentino.echo.rest

import com.github.jvalentino.echo.dto.ResultDto
import com.github.jvalentino.echo.dto.ViewVersionDto
import com.github.jvalentino.echo.entity.AuthUser
import com.github.jvalentino.echo.entity.DocVersion
import com.github.jvalentino.echo.service.DocService
import com.github.jvalentino.echo.service.UserService
import com.github.jvalentino.echo.util.DateGenerator
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletResponse

/**
 * Controller used for viewing document versions
 * @author john.valentino
 */
@Controller
@Slf4j
@RestController
@CompileDynamic
@SuppressWarnings(['UnnecessarySetter', 'UnnecessaryGetter'])
class ViewVersionRest {

    @Autowired
    DocService docService

    @Autowired
    UserService userService

    @GetMapping('/view-versions/{docId}')
    ViewVersionDto index(@PathVariable(value='docId') Long docId) {
        ViewVersionDto result = new ViewVersionDto()
        result.with {
            doc = docService.retrieveDocVersions(docId)
        }

        log.info("Doc ${docId} has ${result.doc.versions.size()} versions")

        result
    }

    // https://www.baeldung.com/servlet-download-file
    @GetMapping('/version/download/{docVersionId}')
    void downloadVersion(@PathVariable(value='docVersionId') Long docVersionId, HttpServletResponse response) {
        DocVersion version = docService.retrieveVersion(docVersionId)

        response.setContentType(version.doc.mimeType)
        response.setHeader('Content-disposition',
                "attachment; filename=${version.doc.name.replaceAll(' ', '')}")

        InputStream is = new ByteArrayInputStream(version.data)
        OutputStream out = response.getOutputStream()

        byte[] buffer = new byte[1048]

        int numBytesRead
        while ((numBytesRead = is.read(buffer)) > 0) {
            out.write(buffer, 0, numBytesRead)
        }
    }

    @PostMapping('/version/new/{docId}')
    ResultDto upload(@RequestParam('file') MultipartFile file, @PathVariable(value='docId') Long docId) {
        AuthUser user = userService.currentLoggedInUser()

        docService.uploadNewVersion(user, file, DateGenerator.date(), docId)

        new ResultDto()
    }

}
