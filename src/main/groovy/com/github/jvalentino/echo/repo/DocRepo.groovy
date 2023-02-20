package com.github.jvalentino.echo.repo

import com.github.jvalentino.echo.entity.Doc
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * Repository interface for Doc, for all your customn HQL needs
 * @author john.valentino
 */
interface DocRepo extends JpaRepository<Doc, Long> {

    @Query('''
        select distinct d from Doc d
        left join fetch d.updatedByUser
        order by d.updatedDateTime DESC
    ''')
    List<Doc> allDocs()

}
