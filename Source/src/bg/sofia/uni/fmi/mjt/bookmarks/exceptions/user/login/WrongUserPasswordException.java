package bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.login;

public class WrongUserPasswordException extends RuntimeException {
    public WrongUserPasswordException(String message) {
        super(message);
    }

    public WrongUserPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}