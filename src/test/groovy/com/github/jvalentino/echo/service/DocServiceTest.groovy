package com.github.jvalentino.echo.service

import com.github.jvalentino.echo.entity.AuthUser
import com.github.jvalentino.echo.entity.Doc
import com.github.jvalentino.echo.entity.DocVersion
import com.github.jvalentino.echo.repo.DocRepo
import com.github.jvalentino.echo.repo.DocVersionRepo
import com.github.jvalentino.echo.service.DocService
import com.github.jvalentino.echo.util.DateUtil
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification
import spock.lang.Subject

class DocServiceTest extends Specification {

    @Subject
    DocService subject

    def setup() {
        subject = new DocService()
        subject.with {
            docRepo = Mock(DocRepo)
            docVersionRepo = Mock(DocVersionRepo)
        }
    }

    def "test countDocuments"() {
        when:
        Long result = subject.countDocuments()

        then:
        1 * subject.docRepo.count() >> 5L

        and:
        result == 5L
    }

    def "test uploadNewDoc"() {
        given:
        AuthUser user = new AuthUser(authUserId:123L)
        MultipartFile file = GroovyMock()
        Date date = DateUtil.toDate('2022-10-31T00:00:00.000+0000')

        when:
        DocVersion result = subject.uploadNewDoc(user, file, date)

        then:
        _ * file.originalFilename >> 'alpha.pdf'
        _ * file.bytes >> 'bravo'.bytes

        and:
        1 * subject.docRepo.save(_) >> { Doc doc ->
            assert doc.name == 'alpha.pdf'
            assert doc.mimeType == 'application/pdf'
            assert doc.createdByUser.authUserId == 123L
            assert doc.updatedByUser.authUserId == 123L
            assert doc.createdDateTime.time == date.time
            assert doc.updatedDateTime.time == date.time
            doc

        }
        1 * subject.docVersionRepo.save(_) >> { DocVersion version ->
            assert version.doc.name == 'alpha.pdf'
            assert new String(version.data) == 'bravo'
            assert version.createdDateTime.time == date.time
            assert version.createdByUser.authUserId == 123L
            assert version.versionNum == 1L
            version
        }

        and:
        result.versionNum == 1L
    }

    def "Test allDocs"() {
        given:
        List<Doc> docs = [new Doc()]

        when:
        List<Doc> results = subject.allDocs()

        then:
        1 * subject.docRepo.allDocs() >> docs

        and:
        results == docs
    }

    def "test uploadNewVersion"() {
        given:
        AuthUser user = new AuthUser(authUserId:123L)
        MultipartFile file = GroovyMock()
        Date date = DateUtil.toDate('2022-10-31T00:00:00.000+0000')
        Long docId = 1L

        and:
        Optional optional = GroovyMock()
        Doc parentDoc = new Doc()

        when:
        DocVersion result = subject.uploadNewVersion(user, file, date, docId)

        then:
        _ * file.originalFilename >> 'alpha.pdf'
        _ * file.bytes >> 'bravo'.bytes

        and:
        1 * subject.docVersionRepo.countForDoc(docId) >> 2
        1 * subject.docRepo.findById(docId) >> optional
        1 * optional.get() >> parentDoc

        and:
        1 * subject.docRepo.save(_) >> { Doc doc ->
            assert doc.name == 'alpha.pdf'
            assert doc.updatedDateTime.time == date.time
            assert doc.updatedByUser.authUserId == user.authUserId
            doc
        }

        and:
        1 * subject.docVersionRepo.save(_) >> { DocVersion version ->
            assert version.doc.name == 'alpha.pdf'
            assert new String(version.data) == 'bravo'
            assert version.createdDateTime.time == date.time
            assert version.createdByUser.authUserId == user.authUserId
            assert version.versionNum == 3L
            version
        }

        and:
        result.doc.name == 'alpha.pdf'
    }
}
