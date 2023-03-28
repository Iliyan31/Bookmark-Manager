package bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password;

public class PasswordNumberException extends RuntimeException {
    public PasswordNumberException(String message) {
        super(message);
    }

    public PasswordNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}