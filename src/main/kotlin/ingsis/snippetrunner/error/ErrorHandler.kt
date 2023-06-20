package ingsis.snippetrunner.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

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

}