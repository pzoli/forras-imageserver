package hu.infokristaly.forrasimageserver.repository

import hu.infokristaly.forrasimageserver.entity.DocLocation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DocLocationCRUDRepository : JpaRepository<DocLocation, Long> {
    fun findByParentId(parentId:Long?): List<DocLocation>?
}