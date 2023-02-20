package com.github.jvalentino.echo.entity

import groovy.transform.CompileDynamic
import org.hibernate.annotations.Type

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.Table
import java.sql.Timestamp

/**
 * Represents an individual version of a document
 * @author john.valentino
 */
@CompileDynamic
@Entity
@Table(name = 'doc_version')
class DocVersion {

    @Id @GeneratedValue
    @Column(name = 'doc_version_id')
    Long docVersionId

    @Column(name = 'version_num')
    Long versionNum

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = 'doc_id', referencedColumnName = 'doc_id')
    Doc doc

    // https://stackoverflow.com/questions/9114510/
    // seam-file-upload-to-postgres-bytea-column-column-is-bytea-but-expression-is-of
    @Lob
    @Type(type='org.hibernate.type.BinaryType')
    @Column(name = 'data', columnDefinition = 'BLOB')
    byte[] data

    @Column(name = 'created_datetime')
    Timestamp createdDateTime

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = 'created_by_user_id', referencedColumnName = 'auth_user_id')
    AuthUser createdByUser

    DocVersion() { }

    DocVersion(Long docVersionId, Long versionNum, Date createdDateTime) {
        this.docVersionId = docVersionId
        this.versionNum = versionNum
        this.createdDateTime = new Timestamp(createdDateTime.time)
    }

}
