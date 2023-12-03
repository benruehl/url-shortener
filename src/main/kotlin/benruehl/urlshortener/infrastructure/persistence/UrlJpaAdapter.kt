package benruehl.urlshortener.infrastructure.persistence

import benruehl.urlshortener.domain.entities.Url
import benruehl.urlshortener.domain.repositories.UrlRepository
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.NoSuchElementException

@Repository
class UrlJpaAdapter(
    var repository: UrlJpaRepository
) : UrlRepository {
    override fun save(originalUrl: String): Url {
        val entity = repository.save(UrlEntity(
            originalUrl = originalUrl
        ))
        return Url(
            originalUrl = originalUrl,
            shortId = entity.entityId.toString()
        )
    }

    override fun findByShortId(shortId: String): Url {
        val entity = repository.findById(shortId.toLong())
        if (entity.isEmpty) {
            throw NoSuchElementException("Url entity with id $shortId does not exist")
        }
        return Url(
            originalUrl = entity.get().originalUrl!!,
            shortId = entity.get().entityId.toString()
        )
    }
}

@Repository
interface UrlJpaRepository : JpaRepository<UrlEntity, Long>

@Entity
open class UrlEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var entityId: Long? = null,

    open var originalUrl: String? = null,
)