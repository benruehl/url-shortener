package benruehl.urlshortener.domain.services

interface ShortIdGenerator {
    fun encode(value: Long): String
    fun decode(shortId: String): Long
}