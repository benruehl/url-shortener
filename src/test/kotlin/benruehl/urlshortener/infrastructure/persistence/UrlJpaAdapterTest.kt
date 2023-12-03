package benruehl.urlshortener.infrastructure.persistence

import benruehl.urlshortener.domain.services.ShortIdGenerator
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import java.lang.Exception
import java.util.NoSuchElementException


@DataJpaTest
@Import(UrlJpaAdapter::class)
class UrlJpaAdapterTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var urlJpaAdapter: UrlJpaAdapter

    @MockkBean
    lateinit var shortIdGenerator: ShortIdGenerator

    @Test
    fun `save should insert entity`() {
        // Arrange
        val shortId = "abcdef"
        val originalUrl = "https://dkbcodefactory.com/"

        every { shortIdGenerator.encode(any()) } returns shortId

        // Act
        val insertedEntity = urlJpaAdapter.save(originalUrl)

        // Assert
        val foundEntity = entityManager.find(UrlEntity::class.java, 1L)
        assertThat(insertedEntity.originalUrl == foundEntity.originalUrl)
    }

    @Test
    fun `findByShortId should find entity when id exists`() {
        // Arrange
        val shortId = "abcdef"
        val originalUrl = "https://dkbcodefactory.com/"
        val preparedEntity = UrlEntity(originalUrl = originalUrl)

        entityManager.persist(preparedEntity)
        entityManager.flush()

        every { shortIdGenerator.decode(any()) } returns preparedEntity.entityId!!

        // Act
        val foundEntity = urlJpaAdapter.findByShortId(shortId)

        // Assert
        assertThat(foundEntity.originalUrl == preparedEntity.originalUrl)
    }

    @Test
    fun `findByShortId should throw NoSuchElementException when id does not exist`() {
        val shortId = "abcdef"

        every { shortIdGenerator.decode(any()) } returns 999999L

        assertThrows(NoSuchElementException::class.java) { urlJpaAdapter.findByShortId(shortId) }
    }

    @Test
    fun `findByShortId should throw NoSuchElementException when id cannot be decoded`() {
        val shortId = "abcdef"

        every { shortIdGenerator.decode(any()) } throws Exception("Any error message.")

        assertThrows(NoSuchElementException::class.java) { urlJpaAdapter.findByShortId(shortId) }
    }
}