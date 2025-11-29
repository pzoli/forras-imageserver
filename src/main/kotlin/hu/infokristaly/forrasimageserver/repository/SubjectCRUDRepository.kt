package hu.infokristaly.forrasimageserver.repository

import hu.infokristaly.forrasimageserver.entity.DocumentSubject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectCRUDRepository : JpaRepository<DocumentSubject, Long> {
    fun findByOrderByValueAsc(): List<DocumentSubject>
}