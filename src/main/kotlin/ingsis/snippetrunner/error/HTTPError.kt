package ingsis.snippetrunner.error

import org.springframework.http.HttpStatus


class HTTPError : Exception {

    var status: HttpStatus
    constructor(message: String, status: HttpStatus) : super(message){
        this.status = status
    }
}