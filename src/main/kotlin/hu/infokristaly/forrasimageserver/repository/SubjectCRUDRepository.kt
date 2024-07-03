package hu.infokristaly.forrasimageserver.repository

import hu.infokristaly.forrasimageserver.entity.DocumentSubject
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectCRUDRepository : CrudRepository<DocumentSubject, Long> {
}