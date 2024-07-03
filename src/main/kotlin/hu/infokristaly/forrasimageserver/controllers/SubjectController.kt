package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.DocumentSubject
import hu.infokristaly.forrasimageserver.repository.SubjectCRUDRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subject")
class SubjectController(@Autowired private val subjectCRUDRepository: SubjectCRUDRepository) {

    @GetMapping("")
    fun getAllSubjects(): List<DocumentSubject> =
        subjectCRUDRepository.findAll().toList()

    //create user
    @PostMapping("")
    fun createSubject(@RequestBody subject: DocumentSubject): ResponseEntity<DocumentSubject> {
        val savedSubject = subjectCRUDRepository.save(subject)
        return ResponseEntity(savedSubject, HttpStatus.CREATED)
    }

    //get user by id
    @GetMapping("/{id}")
    fun getSubjectById(@PathVariable("id") id: Long): ResponseEntity<DocumentSubject> {
        val subject = subjectCRUDRepository.findById(id).orElse(null)
        return if (subject != null) {
            ResponseEntity(subject, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update user
    @PutMapping("/{id}")
    fun updateSubjectById(@PathVariable("id") id: Long, @RequestBody subject: DocumentSubject): ResponseEntity<DocumentSubject> {
        val existingSubject = subjectCRUDRepository.findById(id).orElse(null)

        if (existingSubject == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedSubject = subjectCRUDRepository.save(subject)
        return ResponseEntity(updatedSubject, HttpStatus.OK)
    }

    //delete user
    @DeleteMapping("/{id}")
    fun deleteSubjectById(@PathVariable("id") id: Long): ResponseEntity<DocumentSubject> {
        if (!subjectCRUDRepository.existsById(id)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        subjectCRUDRepository.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}