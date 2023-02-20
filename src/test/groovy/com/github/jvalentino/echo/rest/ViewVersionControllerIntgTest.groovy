package com.github.jvalentino.echo.rest

import com.github.jvalentino.echo.dto.ViewVersionDto
import com.github.jvalentino.echo.entity.AuthUser
import com.github.jvalentino.echo.entity.Doc
import com.github.jvalentino.echo.entity.DocVersion
import com.github.jvalentino.echo.repo.AuthUserRepo
import com.github.jvalentino.echo.util.BaseIntg
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MvcResult
import org.springframework.web.servlet.ModelAndView

import java.sql.Timestamp

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ViewVersionControllerIntgTest extends BaseIntg {

    @Autowired
    AuthUserRepo authUserRepo

    def "test versions"() {
        given:
        this.mockAdminLoggedIn()

        and:
        AuthUser user = authUserRepo.findAdminUser('admin').first()

        Doc doc = new Doc()
        doc.with {
            name = 'alpha.pdf'
            createdByUser = user
            updatedByUser = user
            mimeType = 'application/json'
            createdDateTime = new Timestamp(new Date().time)
            updatedDateTime = new Timestamp(new Date().time)
        }
        this.entityManager.persist(doc)

        DocVersion version = new DocVersion(doc:doc)
        version.with {
            versionNum = 1L
            createdDateTime = new Timestamp(new Date().time)
            createdByUser = user
        }
        this.entityManager.persist(version)

        when:
        MvcResult response = mvc.perform(
                get("/view-versions/${doc.docId}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()

        then:
        ViewVersionDto model = this.toObject(response, ViewVersionDto)
        model.doc.name == 'alpha.pdf'
        model.doc.versions.size() == 1
        model.doc.versions.first().versionNum == 1
    }

    def "test download version"() {
        given:
        this.mockAdminLoggedIn()

        and:
        AuthUser user = authUserRepo.findAdminUser('admin').first()

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

        DocVersion version = new DocVersion(doc:doc)
        version.with {
            versionNum = 1L
            createdDateTime = new Timestamp(new Date().time)
            createdByUser = user
            data = "this is a test".bytes
        }
        this.entityManager.persist(version)

        when:
        MvcResult response = mvc.perform(
                get("/version/download/${version.docVersionId}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()

        then:
        response
        response.response.contentType == 'text/plain'
        new String(response.response.getContentAsByteArray()) == 'this is a test'

    }

}
