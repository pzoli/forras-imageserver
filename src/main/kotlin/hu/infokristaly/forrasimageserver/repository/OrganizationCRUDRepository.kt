package hu.infokristaly.forrasimageserver.repository
import hu.infokristaly.forrasimageserver.entity.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationCRUDRepository : JpaRepository<Organization, Long> {

    fun findByOrderByNameAsc(): List<Organization>
}
