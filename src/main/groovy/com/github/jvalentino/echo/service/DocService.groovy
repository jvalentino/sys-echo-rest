package com.github.jvalentino.echo.service

import com.github.jvalentino.echo.entity.AuthUser
import com.github.jvalentino.echo.entity.DocVersion
import com.github.jvalentino.echo.repo.DocVersionRepo
import com.github.jvalentino.echo.entity.Doc
import com.github.jvalentino.echo.repo.DocRepo
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import java.sql.Timestamp

/**
 * General service for interacting with documents
 * @author john.valentino
 */
@CompileDynamic
@Service
@Slf4j
class DocService {

    @Autowired
    DocRepo docRepo

    @Autowired
    DocVersionRepo docVersionRepo

    Long countDocuments() {
        docRepo.count()
    }

    DocVersion uploadNewDoc(AuthUser user, MultipartFile file, Date date) {
        String ext = file.originalFilename.split('\\.').last()

        Doc parentDoc = new Doc()
        parentDoc.with {
            name = file.originalFilename
            mimeType = "application/${ext}"
            createdByUser = user
            updatedByUser = user
            createdDateTime = new Timestamp(date.time)
            updatedDateTime = new Timestamp(date.time)
        }
        docRepo.save(parentDoc)

        DocVersion version = new DocVersion()
        version.with {
            doc = parentDoc
            data = file.bytes
            createdDateTime = new Timestamp(date.time)
            createdByUser = user
            versionNum = 1
        }
        docVersionRepo.save(version)
    }

    List<Doc> allDocs() {
        docRepo.allDocs()
    }

    Doc retrieveDocVersions(Long docId) {
        Doc doc = docRepo.findById(docId).get()
        doc.versions = docVersionRepo.getVersionsWithoutData(docId)
        doc
    }

    DocVersion retrieveVersion(Long docVersionId) {
        DocVersion version = docVersionRepo.getWithParent(docVersionId).first()
        version
    }

    DocVersion uploadNewVersion(AuthUser user, MultipartFile file, Date date, Long docId) {
        long currentCount = docVersionRepo.countForDoc(docId)

        Doc parentDoc = docRepo.findById(docId).get()
        parentDoc.with {
            name = file.originalFilename
            updatedByUser = user
            updatedDateTime = new Timestamp(date.time)
        }

        docRepo.save(parentDoc)

        DocVersion version = new DocVersion()
        version.with {
            doc = parentDoc
            data = file.bytes
            createdDateTime = new Timestamp(date.time)
            createdByUser = user
            versionNum = currentCount + 1
        }
        docVersionRepo.save(version)
    }

}
