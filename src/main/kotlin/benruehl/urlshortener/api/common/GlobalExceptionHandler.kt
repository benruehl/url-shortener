package benruehl.urlshortener.api.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(exception: NoSuchElementException): ResponseEntity<Any> {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.message)
    }
}