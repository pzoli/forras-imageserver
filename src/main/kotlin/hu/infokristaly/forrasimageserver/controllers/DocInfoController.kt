package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.services.DocInfoService
import hu.infokristaly.forrasimageserver.services.FileInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/docinfo")
class DocInfoController(
    @Autowired private val docInfoService: DocInfoService,
    private val fileInfoService: FileInfoService
) {

    @GetMapping("")
    fun getAllDocInfos(): List<DocInfo> =
        docInfoService.getAllDocInfos()

    @GetMapping("/page/{page}")
    fun getDocInfoByPage(@PathVariable("page") page: Int): ResponseEntity<List<DocInfo>> {
        val docInfos = docInfoService.getDocInfosByPage(page)
        return if (docInfos != null) {
            ResponseEntity(docInfos, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/filter/{filter}/{page}")
    fun getDocInfoByFilter(@PathVariable("filter") filter: String, @PathVariable("page") page: Int): ResponseEntity<List<DocInfo>> {
        val docInfos = docInfoService.findByCommentContainingIgnoreCaseOrOrganizationNameContainingIgnoreCaseOrSubjectValueContainingIgnoreCase(filter,page)
        return if (docInfos != null) {
            ResponseEntity(docInfos, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //create docinfo
    @PostMapping("")
    @ResponseBody
    fun createDocInfo(@RequestBody docInfo: DocInfo): ResponseEntity<DocInfo> {
        try {
            val savedDocInfo = docInfoService.save(docInfo)
            return ResponseEntity(savedDocInfo, HttpStatus.CREATED)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //get docinfo by id
    @GetMapping("/{id}")
    fun getDocInfoById(@PathVariable("id") id: Long): ResponseEntity<DocInfo> {
        val docInfo = docInfoService.getDocInfoById(id).orElse(null)
        return if (docInfo != null) {
            ResponseEntity(docInfo, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update docinfo
    @PutMapping("/{id}")
    fun updateDocInfoById(@PathVariable("id") id: Long, @RequestBody docInfo: DocInfo): ResponseEntity<DocInfo> {
        val existingDocInfo = docInfoService.getDocInfoById(id).orElse(null)

        if (existingDocInfo == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val updatedDocInfo: DocInfo
        try {
            updatedDocInfo = docInfoService.save(docInfo)
        } catch (ex: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity(updatedDocInfo, HttpStatus.OK)
    }

    //delete docinfo
    @DeleteMapping("/{id}")
    @Transactional
    fun deleteDocInfoById(@PathVariable("id") id: Long): ResponseEntity<DocInfo> {
        if (!docInfoService.existsById(id)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        try {
            fileInfoService.deleteAllByDocInfoId(id)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        docInfoService.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}

