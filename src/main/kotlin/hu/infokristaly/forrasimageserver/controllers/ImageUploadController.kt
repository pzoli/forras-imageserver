package hu.infokristaly.forrasimageserver.controllers

import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.entity.FileInfo
import hu.infokristaly.forrasimageserver.repository.*
import org.apache.tomcat.util.http.fileupload.impl.FileItemStreamImpl
import org.hibernate.annotations.NotFound
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/api/file")
class ImageUploadController(
    @Autowired private val subjectCRUDRepository: SubjectCRUDRepository,
    @Autowired private val docInfoRepo: DocInfoRepo,
    @Autowired private val organizationCRUDRepo: OrganizationCRUDRepository,
    @Autowired private val clerkCRUDRepo: ClerkCRUDRepository,
    @Autowired private val fileInfoCRUDRepository: FileInfoCRUDRepository,
) {

    @Value("\${temp.path}")
    private var tempPath: String = ""

    @GetMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
    @ResponseBody
    fun getImagesById(@PathVariable("id") id: Long): ResponseEntity<InputStreamResource> {
        val fileInfo = fileInfoCRUDRepository.findById(id).getOrNull()
        var stream: InputStreamResource? = null
        if (fileInfo != null) {
            stream = InputStreamResource(FileInputStream(Paths.get(tempPath,fileInfo.uniqueFileName).toFile()))
        }
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(stream)
    }

    @RequestMapping(value = arrayOf("/uploadimage"), method = arrayOf( RequestMethod.POST ), consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE ))
    fun uploadScannedImage(@RequestPart(name = "file") file: MultipartFile, @RequestPart(name = "docid") docid: String, @RequestPart(name = "openPaint") openPaint: String) {
        val docInfo = docInfoRepo.findById(docid.toLong());
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val tempFile = File.createTempFile("IMG_" + simpleDateFormat.format(Date()) + "_" ,"."+ file.originalFilename?.substringAfter("."), File(tempPath))
        try {
            file.inputStream.use { `in` ->
                FileOutputStream(tempFile).use { out ->
                    FileCopyUtils.copy(`in`, out)
                }
            }
        } catch (ex: IOException) {
            throw RuntimeException(ex)
        }
        var fileInfo = FileInfo()
        fileInfo.uniqueFileName = tempFile.name
        fileInfo.lenght = tempFile.length()
        fileInfo.docInfo = docInfo.get()
        fileInfoCRUDRepository.save(fileInfo)
        val isWindows = System.getProperty("os.name")
            .lowercase(Locale.getDefault()).startsWith("windows")
        if (isWindows && "true".equals(openPaint)) {
            Runtime.getRuntime()
                .exec(String.format("mspaint.exe " + tempFile.toString()))
        }
    }

    @RequestMapping(value = arrayOf("/upload"), method = arrayOf( RequestMethod.POST ), consumes = arrayOf( MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
    fun fileUpload(@RequestPart("file") file: MultipartFile, @RequestPart("doc") doc: DocInfo) : FileInfo {
        var docInfo = doc
        if (docInfo.subject.id == null) {
            docInfo.subject = subjectCRUDRepository.save(docInfo.subject)
        }
        if (docInfo.organization.id == null) {
            docInfo.organization = organizationCRUDRepo.save(docInfo.organization)
        }
        if (docInfo.clerk != null && docInfo.clerk?.id == null) {
            docInfo.clerk = clerkCRUDRepo.save(docInfo.clerk!!)
        }
        if (docInfo.id == null) {
            docInfo = docInfoRepo.save(docInfo)
        }

        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val tempFile = File.createTempFile("IMG_" + simpleDateFormat.format(Date()) + "_" ,"."+ file.originalFilename?.substringAfter("."), File(tempPath))
        try {
            file.inputStream.use { `in` ->
                FileOutputStream(tempFile).use { out ->
                    FileCopyUtils.copy(`in`, out)
                }
            }
        } catch (ex: IOException) {
            throw RuntimeException(ex)
        }

        var fileInfo = FileInfo()
        fileInfo.uniqueFileName = tempFile.name
        fileInfo.lenght = tempFile.length()
        fileInfo.docInfo = docInfo
        fileInfo = fileInfoCRUDRepository.save(fileInfo)

        return fileInfo
    }

    @RequestMapping(value = arrayOf("/update/{id}"), method = arrayOf( RequestMethod.PUT ), consumes = arrayOf( MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
    fun fileUpdate(@RequestPart("file") file: MultipartFile,@PathVariable("id") id: Long,) : FileInfo? {
        var fileInfo = fileInfoCRUDRepository.findById(id).getOrNull()
        if (fileInfo != null) {
            val tempFile = File(tempPath,fileInfo.uniqueFileName)
            try {
                file.inputStream.use { `in` ->
                    FileOutputStream(tempFile).use { out ->
                        FileCopyUtils.copy(`in`, out)
                    }
                }
                fileInfo.lenght = tempFile.length()
                fileInfo = fileInfoCRUDRepository.save(fileInfo)
                return fileInfo
            } catch (ex: IOException) {
                throw RuntimeException(ex)
            }
        }
        return null
    }

}