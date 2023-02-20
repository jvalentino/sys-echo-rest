package com.github.jvalentino.echo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileDynamic

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * Represents an authorized user
 * @author john.valentino
 */
@CompileDynamic
@Entity
@Table(name = 'auth_user')
class AuthUser {

    @Id @GeneratedValue
    @Column(name = 'auth_user_id')
    Long authUserId

    String email

    String password

    String salt

    @Column(name = 'first_name')
    String firstName

    @Column(name = 'last_name')
    String lastName

    @OneToMany(mappedBy='createdByUser', fetch = FetchType.LAZY)
    @JsonIgnore
    Set<Doc> createdBys

    @OneToMany(mappedBy='updatedByUser', fetch = FetchType.LAZY)
    @JsonIgnore
    Set<Doc> updatedBys

    @OneToMany(mappedBy='createdByUser', fetch = FetchType.LAZY)
    @JsonIgnore
    Set<DocVersion> versionCreatedBys

    @OneToMany(mappedBy='createdByUser', fetch = FetchType.LAZY)
    @JsonIgnore
    Set<DocTask> taskCreatedBys

    @OneToMany(mappedBy='updatedByUser', fetch = FetchType.LAZY)
    @JsonIgnore
    Set<DocTask> taskUpdatedBys

}
