package hb.loggingservice.exception;

public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String msg, Throwable t) {
        super(msg, t);
    }
}
