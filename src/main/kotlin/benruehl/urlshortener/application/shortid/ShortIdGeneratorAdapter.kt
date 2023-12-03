package benruehl.urlshortener.application.shortid

import benruehl.urlshortener.domain.services.ShortIdGenerator
import org.hashids.Hashids
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ShortIdGeneratorAdapter(
    @Value("\${hashids.salt}") val hashidsSalt: String
) : ShortIdGenerator {

    private val hashids = Hashids(hashidsSalt, 6)

    override fun encode(value: Long): String {
        return hashids.encode(value)
    }

    override fun decode(shortId: String): Long {
        return hashids.decode(shortId).first()
    }
}