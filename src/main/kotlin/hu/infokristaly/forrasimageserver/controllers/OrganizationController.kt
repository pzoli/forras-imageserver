package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.Organization
import hu.infokristaly.forrasimageserver.repository.OrganizationCRUDRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/organization")
class OrganizationController(@Autowired private val organizationRepository: OrganizationCRUDRepository) {

    @GetMapping("")
    fun getAllOrganizations(): List<Organization> =
        organizationRepository.findAll().toList()

    //create user
    @PostMapping("")
    fun createOrganization(@RequestBody organization: Organization): ResponseEntity<Organization> {
        val savedSubject = organizationRepository.save(organization)
        return ResponseEntity(savedSubject, HttpStatus.CREATED)
    }

    //get user by id
    @GetMapping("/{id}")
    fun getOrganizationById(@PathVariable("id") id: Long): ResponseEntity<Organization> {
        val subject = organizationRepository.findById(id).orElse(null)
        return if (subject != null) {
            ResponseEntity(subject, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update user
    @PutMapping("/{id}")
    fun updateOrganizationById(@PathVariable("id") id: Long, @RequestBody organization: Organization): ResponseEntity<Organization> {
        val existingOrganization = organizationRepository.findById(id).orElse(null)

        if (existingOrganization == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedOrganization = organizationRepository.save(organization)
        return ResponseEntity(updatedOrganization, HttpStatus.OK)
    }

    //delete user
    @DeleteMapping("/{id}")
    fun deleteOrganizationById(@PathVariable("id") id: Long): ResponseEntity<Organization> {
        if (!organizationRepository.existsById(id)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        organizationRepository.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}