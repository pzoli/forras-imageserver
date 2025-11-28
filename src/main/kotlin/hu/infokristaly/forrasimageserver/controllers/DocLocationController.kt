package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.DocLocation
import hu.infokristaly.forrasimageserver.repository.DocLocationCRUDRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/doclocation")
class DocLocationController(@Autowired private val docLocationCRUDRepository: DocLocationCRUDRepository) {

    @GetMapping("")
    fun getAllLocation(): List<DocLocation> =
        docLocationCRUDRepository.findAll().toList()

    //get DocLocation by id
    @GetMapping("/parent/{id}")
    fun getLocationByParentId(@PathVariable("id") parentId: Long): ResponseEntity<List<DocLocation>> {
        var docLocation: List<DocLocation>? = null
        if (parentId == -1L) {
            docLocation = docLocationCRUDRepository.findByParentId(null)
        } else {
            docLocation = docLocationCRUDRepository.findByParentId(parentId)
        }
        return if (docLocation != null) {
            ResponseEntity(docLocation, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //create DocLocation
    @PostMapping("")
    fun createLocation(@RequestBody docLocation: DocLocation): ResponseEntity<DocLocation> {
        val savedDocLocation = docLocationCRUDRepository.save(docLocation)
        return ResponseEntity(savedDocLocation, HttpStatus.CREATED)
    }

    //get DocLocation by id
    @GetMapping("/{id}")
    fun getLocationById(@PathVariable("id") id: Long): ResponseEntity<DocLocation> {
        val docLocation = docLocationCRUDRepository.findById(id).orElse(null)
        return if (docLocation != null) {
            ResponseEntity(docLocation, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update DocLocation
    @PutMapping("/{id}")
    fun updateLocationById(@PathVariable("id") id: Long, @RequestBody docLocation: DocLocation): ResponseEntity<DocLocation> {
        val existingDocLocation = docLocationCRUDRepository.findById(id).orElse(null)

        if (existingDocLocation == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedDocLocation = docLocationCRUDRepository.save(docLocation)
        return ResponseEntity(updatedDocLocation, HttpStatus.OK)
    }

    //delete DocLocation
    @DeleteMapping("/{id}")
    fun deleteLocationById(@PathVariable("id") id: Long): ResponseEntity<DocLocation> {
        if (!docLocationCRUDRepository.existsById(id)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        docLocationCRUDRepository.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}