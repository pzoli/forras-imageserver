package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.DocLocation
import hu.infokristaly.forrasimageserver.entity.DocumentSubject
import hu.infokristaly.forrasimageserver.repository.DocLocationCRUDRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/doclocation")
class DocLocationController(@Autowired private val docLocationCRUDRepository: DocLocationCRUDRepository) {

    @GetMapping("")
    fun getAllSubjects(): List<DocLocation> =
        docLocationCRUDRepository.findAll().toList()

    //create DocLocation
    @PostMapping("")
    fun createSubject(@RequestBody docLocation: DocLocation): ResponseEntity<DocLocation> {
        val savedDocLocation = docLocationCRUDRepository.save(docLocation)
        return ResponseEntity(savedDocLocation, HttpStatus.CREATED)
    }

    //get DocLocation by id
    @GetMapping("/{id}")
    fun getSubjectById(@PathVariable("id") id: Long): ResponseEntity<DocLocation> {
        val docLocation = docLocationCRUDRepository.findById(id).orElse(null)
        return if (docLocation != null) {
            ResponseEntity(docLocation, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update DocLocation
    @PutMapping("/{id}")
    fun updateSubjectById(@PathVariable("id") id: Long, @RequestBody docLocation: DocLocation): ResponseEntity<DocLocation> {
        val existingDocLocation = docLocationCRUDRepository.findById(id).orElse(null)

        if (existingDocLocation == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedDocLocation = docLocationCRUDRepository.save(docLocation)
        return ResponseEntity(updatedDocLocation, HttpStatus.OK)
    }

    //delete DocLocation
    @DeleteMapping("/{id}")
    fun deleteSubjectById(@PathVariable("id") id: Long): ResponseEntity<DocLocation> {
        if (!docLocationCRUDRepository.existsById(id)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        docLocationCRUDRepository.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}