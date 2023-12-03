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
        val originalId = "https://dkbcodefactory.com/"
        val requestContent = UrlRequest(originalId)

        every { repository.save(originalId) } returns Url(shortId = shortId, originalUrl = originalId);

        mockMvc.perform(post("/u")
                .content(mapper.writeValueAsString(requestContent))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.shortId").value(shortId))
    }

    @Test
    fun `get should return redirect to original url`() {
        val shortId = "abcdef"
        val originalId = "https://dkbcodefactory.com/"

        every { repository.findByShortId(shortId) } returns Url(shortId = shortId, originalUrl = originalId);

        mockMvc.perform(get("/u/$shortId"))
            .andExpect(status().`is`(302))
            .andExpect(redirectedUrl(originalId))
    }
}