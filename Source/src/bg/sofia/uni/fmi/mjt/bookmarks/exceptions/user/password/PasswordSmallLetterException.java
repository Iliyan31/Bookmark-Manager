package bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password;

public class PasswordSmallLetterException extends RuntimeException {
    public PasswordSmallLetterException(String message) {
        super(message);
    }

    public PasswordSmallLetterException(String message, Throwable cause) {
        super(message, cause);
    }
}