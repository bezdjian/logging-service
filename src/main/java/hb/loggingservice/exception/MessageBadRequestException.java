package hb.loggingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MessageBadRequestException extends RuntimeException {
    public MessageBadRequestException(String msg) {
        super(msg);
    }
}
