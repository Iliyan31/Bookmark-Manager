package bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password;

public class PasswordCapitalLetterException extends RuntimeException {
    public PasswordCapitalLetterException(String message) {
        super(message);
    }

    public PasswordCapitalLetterException(String message, Throwable cause) {
        super(message, cause);
    }
}