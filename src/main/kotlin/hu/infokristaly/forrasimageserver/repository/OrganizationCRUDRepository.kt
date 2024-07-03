package hu.infokristaly.forrasimageserver.repository
import hu.infokristaly.forrasimageserver.entity.Organization
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationCRUDRepository : CrudRepository<Organization, Long> {
}
