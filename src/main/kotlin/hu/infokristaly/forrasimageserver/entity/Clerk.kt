package hu.infokristaly.forrasimageserver.entity

import jakarta.persistence.*

@Entity
@Cacheable(value = true)
@Table(name = "clerk")
class Clerk(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    var id: Int?,

    @Basic
    var name: String,

    @Basic
    var phone: String,

    @Basic
    @ManyToOne
    var organization: Organization,

) {

}