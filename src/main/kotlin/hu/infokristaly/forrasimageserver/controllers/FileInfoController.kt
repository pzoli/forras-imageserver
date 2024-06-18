package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.FileInfo
import hu.infokristaly.forrasimageserver.repository.FileInfoRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/fileinfo")
class FileInfoController(@Autowired private val fileInfoRepo: FileInfoRepo) {

    @GetMapping("")
    fun getAllFileInfo(): List<FileInfo> =
        fileInfoRepo.findAll().toList()

    //create user
    @PostMapping("")
    fun createFileInfo(@RequestBody fileInfo: FileInfo): ResponseEntity<FileInfo> {
        val savedFileInfo = fileInfoRepo.save(fileInfo)
        return ResponseEntity(savedFileInfo, HttpStatus.CREATED)
    }

    //get user by id
    @GetMapping("/{id}")
    fun getFileInfoById(@PathVariable("id") id: Int): ResponseEntity<FileInfo> {
        val fileInfo = fileInfoRepo.findById(id).orElse(null)
        return if (fileInfo != null) {
            ResponseEntity(fileInfo, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update user
    @PutMapping("/{id}")
    fun updateFileInfoById(@PathVariable("id") id: Int, @RequestBody fileInfo: FileInfo): ResponseEntity<FileInfo> {
        val existingFileInfo = fileInfoRepo.findById(id).orElse(null)

        if (existingFileInfo == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedFileInfo = fileInfoRepo.save(fileInfo)
        return ResponseEntity(updatedFileInfo, HttpStatus.OK)
    }

    //delete user
    @DeleteMapping("/{id}")
    fun deleteDocInfoById(@PathVariable("id") id: Int): ResponseEntity<FileInfo> {
        if (!fileInfoRepo.existsById(id)){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        fileInfoRepo.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}