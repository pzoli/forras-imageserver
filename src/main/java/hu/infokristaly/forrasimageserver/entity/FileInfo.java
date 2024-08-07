package hu.infokristaly.forrasimageserver.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Cacheable(value = true)
@Table(name = "fileInfo")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -1021160915376137624L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Basic
    @Column(name = "uniqueFileName")
    private String uniqueFileName;

    @Basic
    @Column(name = "lenght")
    private Long lenght;

    @ManyToOne
    @JoinColumn(name="doc_info_id")
    private DocInfo docInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueFileName() {
        return uniqueFileName;
    }

    public void setUniqueFileName(String uniqueFileName) {
        this.uniqueFileName = uniqueFileName;
    }

    public Long getLenght() {
        return lenght;
    }

    public void setLenght(Long lenght) {
        this.lenght = lenght;
    }

    public DocInfo getDocInfo() {
        return docInfo;
    }

    public void setDocInfo(DocInfo docInfo) {
        this.docInfo = docInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileInfo fileInfo)) return false;
        return Objects.equals(id, fileInfo.id) && Objects.equals(uniqueFileName, fileInfo.uniqueFileName) && Objects.equals(lenght, fileInfo.lenght) && Objects.equals(docInfo, fileInfo.docInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueFileName, lenght, docInfo);
    }
}
