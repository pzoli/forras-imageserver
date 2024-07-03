package hu.infokristaly.forrasimageserver.repository

import hu.infokristaly.forrasimageserver.entity.Clerk
import org.springframework.data.repository.CrudRepository

interface ClerkCRUDRepository : CrudRepository<Clerk, Long> {
}