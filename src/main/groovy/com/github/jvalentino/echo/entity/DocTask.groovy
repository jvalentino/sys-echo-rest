package com.github.jvalentino.echo.entity

import groovy.transform.CompileDynamic

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import java.sql.Timestamp

/**
 * A task associated with a document
 * @author john.valentino
 */
@CompileDynamic
@Entity
@Table(name = 'doc_task')
class DocTask {

    @Id @GeneratedValue
    @Column(name = 'doc_task_id')
    Long docTaskId

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = 'doc_id', referencedColumnName = 'doc_id')
    Doc doc

    String name

    String status

    String content

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = 'created_by_user_id', referencedColumnName = 'auth_user_id')
    AuthUser createdByUser

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = 'updated_by_user_id', referencedColumnName = 'auth_user_id')
    AuthUser updatedByUser

    @Column(name = 'created_datetime')
    Timestamp createdDateTime

    @Column(name = 'updated_datetime')
    Timestamp updatedDateTime

}
