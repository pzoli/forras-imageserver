package hu.infokristaly.forrasimageserver.repository

import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.entity.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DocInfoRepo : JpaRepository<DocInfo, Int> {

    @Query("FROM DocInfo WHERE subject = :subject")
    fun findAllByFirstName(@Param("subject") subject: Subject):
            List<DocInfo>
}