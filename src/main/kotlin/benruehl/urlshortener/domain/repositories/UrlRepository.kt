package benruehl.urlshortener.domain.repositories

import benruehl.urlshortener.domain.entities.Url

interface UrlRepository {
    fun save(originalUrl: String): Url
    fun findByShortId(shortId: String): Url
}