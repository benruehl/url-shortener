package benruehl.urlshortener.infrastructure.persistence

import benruehl.urlshortener.domain.entities.Url
import benruehl.urlshortener.domain.repositories.UrlRepository
import benruehl.urlshortener.domain.services.ShortIdGenerator
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class UrlJpaAdapter(
    val repository: UrlJpaRepository,
    val shortIdGenerator: ShortIdGenerator,
) : UrlRepository {

    override fun save(originalUrl: String): Url {
        val entity = repository.save(UrlEntity(
            originalUrl = originalUrl
        ))
        return Url(
            originalUrl = originalUrl,
            shortId = shortIdGenerator.encode(entity.entityId!!)
        )
    }

    override fun findByShortId(shortId: String): Url {
        val entityId: Long
        try {
            entityId = shortIdGenerator.decode(shortId)
        } catch (e: Exception) {
            throw NoSuchElementException("Short id $shortId could not be decoded.")
        }

        val entity = repository.findById(entityId)

        if (entity.isEmpty) {
            throw NoSuchElementException("Url entity with id $shortId does not exist.")
        }

        return Url(
            originalUrl = entity.get().originalUrl!!,
            shortId = shortId
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