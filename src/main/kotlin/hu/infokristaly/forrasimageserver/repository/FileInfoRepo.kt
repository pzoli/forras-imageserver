package hu.infokristaly.forrasimageserver.repository

import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.entity.FileInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FileInfoRepo : JpaRepository<FileInfo, Long> {

    @Query("FROM FileInfo WHERE docInfo.id = :docInfoId order by id")
    fun findAllByDocInfoId(@Param("docInfoId") docInfoId: Long): List<FileInfo>

    fun deleteFileInfosByDocInfoId(docInfoId: Long)
}