package bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.manager;

public class UserNotLoggedException extends RuntimeException {
    public UserNotLoggedException(String message) {
        super(message);
    }

    public UserNotLoggedException(String message, Throwable cause) {
        super(message, cause);
    }
}