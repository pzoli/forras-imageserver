package hu.infokristaly.forrasimageserver.repository

import hu.infokristaly.forrasimageserver.entity.FileInfo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FileInfoCRUDRepository : CrudRepository<FileInfo, Int> {
}