package hu.infokristaly.forrasimageserver.entity

import jakarta.persistence.*

@Entity
@Cacheable(value = true)
@Table(name = "document_subject")
class DocumentSubject (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    var id: Long?,

    @Basic
    var value: String
) {
}