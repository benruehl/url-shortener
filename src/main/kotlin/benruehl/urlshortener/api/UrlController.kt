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
    fun shortenUrl(@RequestBody url: UrlRequest): UrlResponse {
        val savedUrl = repository.save(url.originalUrl)
        return UrlResponse(shortId = savedUrl.shortId)
    }

    @GetMapping("/{shortId}")
    fun redirectToOriginal(@PathVariable shortId: String): RedirectView {
        val savedUrl = repository.findByShortId(shortId)
        return RedirectView(savedUrl.originalUrl)
    }
}