package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.repository.DocInfoRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/docinfo")
class DocInfoController(@Autowired private val docInfoRepo: DocInfoRepo) {

    @GetMapping("")
    fun getAllDocInfos(): List<DocInfo> =
        docInfoRepo.findAll().toList()

    //create user
    @PostMapping("")
    @ResponseBody
    fun createDocInfo(@RequestBody docInfo: DocInfo): ResponseEntity<DocInfo> {
        val savedDocInfo = docInfoRepo.save(docInfo)
        return ResponseEntity(savedDocInfo, HttpStatus.CREATED)
    }

    //get user by id
    @GetMapping("/{id}")
    fun getDocInfoById(@PathVariable("id") id: Int): ResponseEntity<DocInfo> {
        val docInfo = docInfoRepo.findById(id).orElse(null)
        return if (docInfo != null) {
            ResponseEntity(docInfo, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update user
    @PutMapping("/{id}")
    fun updateDocInfoById(@PathVariable("id") id: Int, @RequestBody docInfo: DocInfo): ResponseEntity<DocInfo> {
        val existingDocInfo = docInfoRepo.findById(id).orElse(null)

        if (existingDocInfo == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedDocInfo = docInfoRepo.save(docInfo)
        return ResponseEntity(updatedDocInfo, HttpStatus.OK)
    }

    //delete user
    @DeleteMapping("/{id}")
    fun deleteDocInfoById(@PathVariable("id") id: Int): ResponseEntity<DocInfo> {
        if (!docInfoRepo.existsById(id)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        docInfoRepo.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}