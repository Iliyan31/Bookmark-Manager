package bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }
}