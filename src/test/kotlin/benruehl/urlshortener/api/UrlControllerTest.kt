package benruehl.urlshortener.api

import benruehl.urlshortener.domain.entities.Url
import benruehl.urlshortener.domain.repositories.UrlRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.NoSuchElementException

@WebMvcTest
class UrlControllerTest(
    @Autowired val mockMvc: MockMvc
) {

    private val mapper = jacksonObjectMapper()

    @MockkBean
    lateinit var repository: UrlRepository

    @Test
    fun `post should return ok and short id`() {
        val shortId = "abcdef"
        val originalUrl = "https://dkbcodefactory.com/"
        val requestContent = UrlCreateRequest(originalUrl)

        every { repository.save(originalUrl) } returns Url(shortId = shortId, originalUrl = originalUrl)

        mockMvc.perform(post("/u")
                .content(mapper.writeValueAsString(requestContent))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.shortId").value(shortId))
    }

    @Test
    fun `get should return redirect to original url when id exists`() {
        val shortId = "abcdef"
        val originalUrl = "https://dkbcodefactory.com/"

        every { repository.findByShortId(shortId) } returns Url(shortId = shortId, originalUrl = originalUrl)

        mockMvc.perform(get("/u/$shortId"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl(originalUrl))
    }

    @Test
    fun `get should return not found when id does not exist`() {
        val shortId = "abcdef"

        every { repository.findByShortId(shortId) } throws NoSuchElementException()

        mockMvc.perform(get("/u/$shortId"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `get raw should return ok and original url when id exists`() {
        val shortId = "abcdef"
        val originalUrl = "https://dkbcodefactory.com/"

        every { repository.findByShortId(shortId) } returns Url(shortId = shortId, originalUrl = originalUrl)

        mockMvc.perform(get("/u/$shortId/raw"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
    }

    @Test
    fun `get raw should return not found when id does not exist`() {
        val shortId = "abcdef"

        every { repository.findByShortId(shortId) } throws NoSuchElementException()

        mockMvc.perform(get("/u/$shortId/raw"))
                .andExpect(status().isNotFound)
    }
}