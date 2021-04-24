package hb.loggingservice.exception;

public class MessageException extends RuntimeException {
    public MessageException(String msg, Throwable t) {
        super(msg, t);
    }
}
