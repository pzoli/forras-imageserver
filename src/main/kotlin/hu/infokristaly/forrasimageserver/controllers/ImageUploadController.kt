package hu.infokristaly.forrasimageserver.controllers

import com.google.gson.Gson
import hu.infokristaly.forrasimageserver.entity.DocInfo
import hu.infokristaly.forrasimageserver.entity.FileInfo
import hu.infokristaly.forrasimageserver.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.util.*
import kotlin.io.path.Path


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
    private val tempPath: String? = null

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

        val localFile = Path(tempPath.toString(), file.originalFilename.toString()).toFile()
        try {
            file.inputStream.use { `in` ->
                FileOutputStream(localFile).use { out ->
                    FileCopyUtils.copy(`in`, out)
                }
            }
        } catch (ex: IOException) {
            throw RuntimeException(ex)
        }

        var fileInfo = FileInfo()
        fileInfo.uniqueFileName = file.originalFilename
        fileInfo.lenght = localFile.length()
        fileInfo.docInfo = docInfo
        fileInfo = fileInfoCRUDRepository.save(fileInfo)

        val location: URI = fromCurrentRequest().buildAndExpand().toUri()

        return fileInfo
    }

}