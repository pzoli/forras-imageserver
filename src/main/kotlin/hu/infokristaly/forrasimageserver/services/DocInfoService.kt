package hu.infokristaly.forrasimageserver.services

import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.repository.DocInfoRepo
import hu.infokristaly.forrasimageserver.repository.FileInfoRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class DocInfoService(@Autowired private val docInfoRepo: DocInfoRepo, @Autowired private val fileInfoRepo: FileInfoRepo) {

    val PAGE_SIZE = 20

    fun createDocInfo(docInfo: DocInfo): DocInfo =
        docInfoRepo.save(docInfo)

    fun getAllDocInfos() =
        docInfoRepo.findAllOrderByCreatedAtOrderByDirectionDesc().toList()

    fun save(docInfo: DocInfo): DocInfo =
        docInfoRepo.save(docInfo)

    fun getDocInfoById(id: Long): Optional<DocInfo> =
        docInfoRepo.findById(id)

    fun existsById(id: Long): Boolean =
        docInfoRepo.existsById(id)

    fun deleteById(id: Long) =
        docInfoRepo.deleteById(id)

    fun getDocInfosByPage(pageNumber: Int): List<DocInfo> {
        val pageable: Pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE,Sort.by("createdAt").descending())
        val result = docInfoRepo.findAll(pageable)
        val resultList = result.toList()
        return resultList
    }

}