package com.github.jvalentino.echo.service

import com.github.jvalentino.echo.entity.AuthUser
import com.github.jvalentino.echo.entity.Doc
import com.github.jvalentino.echo.entity.DocVersion
import com.github.jvalentino.echo.repo.AuthUserRepo
import com.github.jvalentino.echo.util.BaseIntg
import com.github.jvalentino.echo.util.DateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile

import java.sql.Timestamp

class DocServiceIntgTest extends BaseIntg {

    @Autowired
    DocService docService

    @Autowired
    AuthUserRepo authUserRepo

    def "test uploadNewDoc"() {
        given:
        this.mockAdminLoggedIn()
        AuthUser user = authUserRepo.findAdminUser('admin').first()

        and:
        MultipartFile file = GroovyMock()
        Date date = DateUtil.toDate('2022-10-31T00:00:00.000+0000')

        when:
        DocVersion result = docService.uploadNewDoc(user, file, date)

        then:
        _ * file.originalFilename >> 'alpha.pdf'
        _ * file.bytes >> [0]

        and:
        result.versionNum == 1
        result.data == [0]
        result.createdDateTime.time == date.time
        result.createdByUser == user
    }

    def "test uploadNewVersion"() {
        given:
        this.mockAdminLoggedIn()
        AuthUser user = authUserRepo.findAdminUser('admin').first()

        and:
        Doc doc = new Doc()
        doc.with {
            name = 'alpha.txt'
            createdByUser = user
            updatedByUser = user
            mimeType = 'text/plain'
            createdDateTime = new Timestamp(new Date().time)
            updatedDateTime = new Timestamp(new Date().time)
        }
        this.entityManager.persist(doc)

        and:
        MultipartFile file = GroovyMock()
        Date date = DateUtil.toDate('2022-10-31T00:00:00.000+0000')

        when:
        DocVersion result = docService.uploadNewVersion(user, file, date, doc.docId)

        then:
        _ * file.originalFilename >> 'alpha.txt'
        _ * file.bytes >> "hi".bytes

        and:
        doc.updatedDateTime.time == date.time
        new String(result.data) == 'hi'
        result.versionNum == 1
    }
}
