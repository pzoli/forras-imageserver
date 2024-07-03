package hu.infokristaly.forrasimageserver.entity

import jakarta.persistence.*
import java.util.Date

enum class DocumentDirection {
    IN, OUT
}

@Entity
@Cacheable(value = true)
@Table(name = "docInfo")
data class DocInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    var id: Long?,

    @Basic
    @ManyToOne
    var subject: DocumentSubject,

    @Basic
    var direction: DocumentDirection,

    @Basic
    var createdAt: Date,

    @Basic
    @ManyToOne
    var organization: Organization,

    @Basic
    @ManyToOne
    var clerk: Clerk?,

    ) {
}