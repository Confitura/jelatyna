package pl.confitura.jelatyna;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<Error> accessDeniedException(Exception ex) {
        return Error.create(ex, FORBIDDEN).buildResponse();
    }

    private static class Error {

        private String message;
        private int httpStatus = SERVICE_UNAVAILABLE.value();

        public Error(String message, HttpStatus httpStatus) {
            this.message = message;
            this.httpStatus = httpStatus.value();
        }

        public static Error create(Exception ex, HttpStatus status) {
            return create(ex.getLocalizedMessage(), status);
        }

        public static Error create(String message, HttpStatus notFound) {
            return new Error(message, notFound);
        }

        public ResponseEntity<Error> buildResponse() {
            return new ResponseEntity<>(this, HttpStatus.valueOf(httpStatus));
        }

        public String getMessage() {
            return message;
        }


    }
}
