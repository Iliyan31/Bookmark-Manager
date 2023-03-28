package bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password;

public class PasswordLengthException extends RuntimeException {
    public PasswordLengthException(String message) {
        super(message);
    }

    public PasswordLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}