package benruehl.urlshortener.api

data class UrlRequest(val originalUrl: String)

data class UrlResponse(val shortId: String)