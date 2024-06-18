package hu.infokristaly.forrasimageserver.entity

import jakarta.persistence.*

@Entity
@Cacheable(value = true)
@Table(name = "subject")
class Subject (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    var id: Int?,

    @Basic
    var value: String
) {
}