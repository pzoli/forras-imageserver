package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.entity.FileInfo
import hu.infokristaly.forrasimageserver.repository.FileInfoRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import kotlin.io.path.Path

@RestController
@RequestMapping("/api/fileinfo")
class FileInfoController(@Autowired private val fileInfoRepo: FileInfoRepo) {
    @Value("\${temp.path}")
    private var tempPath: String = ""

    @GetMapping("")
    fun getAllFileInfo(): List<FileInfo> =
        fileInfoRepo.findAll().toList()

    @GetMapping("/bydocinfoid/{id}")
    fun getFileInfoByDocInfoId(@PathVariable("id") id: Long): List<FileInfo> =
        fileInfoRepo.findAllByDocInfoId(id).toList()

    //create FileInfo
    @PostMapping("")
    fun createFileInfo(@RequestBody fileInfo: FileInfo): ResponseEntity<FileInfo> {
        val savedFileInfo = fileInfoRepo.save(fileInfo)
        return ResponseEntity(savedFileInfo, HttpStatus.CREATED)
    }

    //get FileInfo by id
    @GetMapping("/{id}")
    fun getFileInfoById(@PathVariable("id") id: Long): ResponseEntity<FileInfo> {
        val fileInfo = fileInfoRepo.findById(id).orElse(null)
        return if (fileInfo != null) {
            ResponseEntity(fileInfo, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update FileInfo
    @PutMapping("/{id}")
    fun updateFileInfoById(@PathVariable("id") id: Long, @RequestBody fileInfo: FileInfo): ResponseEntity<FileInfo> {
        val existingFileInfo = fileInfoRepo.findById(id).orElse(null)

        if (existingFileInfo == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedFileInfo = fileInfoRepo.save(fileInfo)
        return ResponseEntity(updatedFileInfo, HttpStatus.OK)
    }

    //delete FileInfo
    @DeleteMapping("/{id}")
    fun deleteFileInfoById(@PathVariable("id") id: Long): ResponseEntity<FileInfo> {
        val existingFileInfo = fileInfoRepo.findById(id).orElse(null)
        if (existingFileInfo == null){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        fileInfoRepo.deleteById(id)
        Files.delete(Path(tempPath,existingFileInfo.uniqueFileName))
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}