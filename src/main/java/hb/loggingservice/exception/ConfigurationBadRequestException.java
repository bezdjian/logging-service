package hb.loggingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConfigurationBadRequestException extends RuntimeException {
    public ConfigurationBadRequestException(String msg) {
        super(msg);
    }
}
