package hu.infokristaly.forrasimageserver.entity

import jakarta.persistence.*

@Entity
@Cacheable(value = true)
@Table(name = "organization")
class Organization(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    var id: Long?,

    @Basic
    var name: String,

    @Basic
    var hqAddress: String,

    @Basic
    var hqPhone: String,

    @Basic
    @Version
    var version: Long = 0L

) {
}