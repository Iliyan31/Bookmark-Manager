package bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password;

public class PasswordSpecialCharacterException  extends RuntimeException {
    public PasswordSpecialCharacterException(String message) {
        super(message);
    }

    public PasswordSpecialCharacterException(String message, Throwable cause) {
        super(message, cause);
    }
}