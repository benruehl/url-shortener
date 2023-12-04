package benruehl.urlshortener.api

import benruehl.urlshortener.domain.repositories.UrlRepository
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/u")
class UrlController(
    val repository: UrlRepository
) {

    @PostMapping
    fun shortenUrl(@RequestBody url: UrlCreateRequest): UrlCreateResponse {
        val savedUrl = repository.save(url.originalUrl)
        return UrlCreateResponse(shortId = savedUrl.shortId)
    }

    @GetMapping("/{shortId}")
    fun redirectToOriginal(@PathVariable shortId: String): RedirectView {
        val savedUrl = repository.findByShortId(shortId)
        return RedirectView(savedUrl.originalUrl)
    }

    @GetMapping("/{shortId}/raw")
    fun getOriginal(@PathVariable shortId: String): UrlReadResponse {
        val savedUrl = repository.findByShortId(shortId)
        return UrlReadResponse(originalUrl = savedUrl.originalUrl)
    }
}