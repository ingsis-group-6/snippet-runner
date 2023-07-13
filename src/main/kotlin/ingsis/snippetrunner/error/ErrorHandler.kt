package ingsis.snippetrunner.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException

@ControllerAdvice
class ErrorHandler {

    @ExceptionHandler(HTTPError::class)
    fun handleException(ex: HTTPError?): ResponseEntity<String?>? {
        return ResponseEntity.status(ex!!.status)
            .body(ex!!.message)
    }

    @ExceptionHandler(Error::class)
    fun handleUserNotFoundException(ex: Error): ResponseEntity<String?>? {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.message)
    }

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleHttpServiceException(ex: HttpClientErrorException): ResponseEntity<String?>? {
        return ResponseEntity.status(ex.statusCode.value())
            .body(ex.message)
    }

}