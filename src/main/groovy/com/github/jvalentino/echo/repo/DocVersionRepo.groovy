package com.github.jvalentino.echo.repo

import com.github.jvalentino.echo.entity.DocVersion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * Repository interface for Doc, for all your customn HQL needs
 * @author john.valentino
 */
interface DocVersionRepo extends JpaRepository<DocVersion, Long> {

    @Query('''
        select new com.github.jvalentino.echo.entity.DocVersion(
            v.docVersionId,
            v.versionNum,
            v.createdDateTime
        )
        from DocVersion v
        where v.doc.docId = ?1
        order by v.versionNum
    ''')
    List<DocVersion> getVersionsWithoutData(Long docId)

    @Query('''
        select distinct v from DocVersion v
        left join fetch v.doc
        where v.docVersionId = ?1
    ''')
    List<DocVersion> getWithParent(Long docVersionId)

    @Query('select count(v) from DocVersion v where v.doc.docId = ?1')
    Long countForDoc(Long docId)

}
