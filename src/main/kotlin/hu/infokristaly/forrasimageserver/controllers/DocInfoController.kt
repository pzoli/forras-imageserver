package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.repository.DocInfoRepo
import hu.infokristaly.forrasimageserver.repository.FileInfoRepo
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/api/docinfo")
class DocInfoController(@Autowired private val docInfoRepo: DocInfoRepo, @Autowired private val fileInfoRepo: FileInfoRepo) {

    @Value("\${temp.path}")
    private var tempPath: String = ""

    @GetMapping("")
    fun getAllDocInfos(): List<DocInfo> =
        docInfoRepo.findAllOrderByCreatedAtOrderByDirectionDesc().toList()

    //create docinfo
    @PostMapping("")
    @ResponseBody
    fun createDocInfo(@RequestBody docInfo: DocInfo): ResponseEntity<DocInfo> {
        val savedDocInfo = docInfoRepo.save(docInfo)
        return ResponseEntity(savedDocInfo, HttpStatus.CREATED)
    }

    //get docinfo by id
    @GetMapping("/{id}")
    fun getDocInfoById(@PathVariable("id") id: Long): ResponseEntity<DocInfo> {
        val docInfo = docInfoRepo.findById(id).orElse(null)
        return if (docInfo != null) {
            ResponseEntity(docInfo, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update docinfo
    @PutMapping("/{id}")
    fun updateDocInfoById(@PathVariable("id") id: Long, @RequestBody docInfo: DocInfo): ResponseEntity<DocInfo> {
        val existingDocInfo = docInfoRepo.findById(id).orElse(null)

        if (existingDocInfo == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedDocInfo = docInfoRepo.save(docInfo)
        return ResponseEntity(updatedDocInfo, HttpStatus.OK)
    }

    //delete docinfo
    @DeleteMapping("/{id}")
    @Transactional
    fun deleteDocInfoById(@PathVariable("id") id: Long): ResponseEntity<DocInfo> {
        if (!docInfoRepo.existsById(id)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        try {
            val fileInfos = fileInfoRepo.findAllByDocInfoId(id)
            for (fileInfo in fileInfos) {
                val file = Paths.get(tempPath,fileInfo.uniqueFileName)
                Files.deleteIfExists(file)
            }
            fileInfoRepo.deleteFileInfosByDocInfoId(id)
            docInfoRepo.deleteById(id)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}