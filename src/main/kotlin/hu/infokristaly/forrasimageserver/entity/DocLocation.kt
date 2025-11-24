package hu.infokristaly.forrasimageserver.entity

import jakarta.persistence.Basic
import jakarta.persistence.Cacheable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Cacheable(value = true)
@Table(name = "document_location")
data class DocLocation (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    var id: Long?,

    @Basic
    var name: String,

    @ManyToOne
    var parent: DocLocation?,

    ) {

}