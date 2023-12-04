package benruehl.urlshortener.api

// Create Url
data class UrlCreateRequest(val originalUrl: String)
data class UrlCreateResponse(val shortId: String)


// Read Url
data class UrlReadResponse(val originalUrl: String)

