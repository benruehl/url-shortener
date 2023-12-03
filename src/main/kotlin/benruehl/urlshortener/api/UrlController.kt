package benruehl.urlshortener.api

import benruehl.urlshortener.domain.repositories.UrlRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/u")
class UrlController(
    val repository: UrlRepository
) {

    @PostMapping
    fun shortenUrl(@RequestBody url: UrlRequest): UrlResponse {
        val savedUrl = repository.save(url.originalUrl)
        return UrlResponse(shortId = savedUrl.shortId)
    }

    @GetMapping
    fun redirectToOriginal(): String {
        return "Hello"
    }
}