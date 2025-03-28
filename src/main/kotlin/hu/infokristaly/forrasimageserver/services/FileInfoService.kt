package hu.infokristaly.forrasimageserver.services

import hu.infokristaly.forrasimageserver.repository.FileInfoRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class FileInfoService(@Autowired private val fileInfoRepo: FileInfoRepo) {
    @Value("\${temp.path}")
    private var tempPath: String = ""

    fun deleteAllByDocInfoId(id: Long) {
        val fileInfos = fileInfoRepo.findAllByDocInfoId(id)
        for (fileInfo in fileInfos) {
            val file = Paths.get(tempPath,fileInfo.uniqueFileName)
            Files.deleteIfExists(file)
        }
        fileInfoRepo.deleteByDocInfoId(id)
    }
}
