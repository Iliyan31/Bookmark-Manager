package bg.sofia.uni.fmi.mjt.bookmarks.authentication;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.user.UserData;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordCapitalLetterException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordLengthException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordNumberException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordSmallLetterException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordSpecialCharacterException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.UserAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;

import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;

public abstract class Validator {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String REGEX_FOR_CONTAINING_NUMBERS_IN_PASSWORD = ".*\\d.*";
    private static final String REGEX_FOR_CAPITAL_LETTER_IN_PASSWORD = ".*[A-Z].*";
    private static final String REGEX_FOR_SMALL_LETTER_IN_PASSWORD = ".*[a-z].*";
    private static final String REGEX_FOR_SPECIFIC_SYMBOL_1 = ".*\\p{Punct}.*";
    private static final String REGEX_FOR_SPECIFIC_SYMBOL_2 = ".*\\p{IsPunctuation}.*";

    protected void validateUsername(String username) {
        if (username == null || username.isEmpty() || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null, empty or blank!");
        }
    }

    protected void validatePassword(String password) {
        if (password == null || password.isEmpty() || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null, empty or blank!");
        }
    }

    protected void validateUserDatabaseManager(UserDatabase userDatabaseManager) {
        if (userDatabaseManager == null) {
            throw new IllegalArgumentException("The user database passed cannot be null!");
        }
    }

    protected void validateSocketChannel(SocketChannel clientChannel) {
        if (clientChannel == null) {
            throw new IllegalArgumentException("The client channel passed cannot be null!");
        }
    }

    protected void validatePasswordForLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new PasswordLengthException("The password should be minimum 8 characters!");
        }
    }

    protected void validatePasswordNumber(String password) {
        if (!password.matches(REGEX_FOR_CONTAINING_NUMBERS_IN_PASSWORD)) {
            throw new PasswordNumberException("The password should contain at least one number!");
        }
    }

    protected void validatePasswordCapitalLetter(String password) {
        if (!password.matches(REGEX_FOR_CAPITAL_LETTER_IN_PASSWORD)) {
            throw new PasswordCapitalLetterException("The password should contain at least one capital letter!");
        }
    }

    protected void validatePasswordSmallLetter(String password) {
        if (!password.matches(REGEX_FOR_SMALL_LETTER_IN_PASSWORD)) {
            throw new PasswordSmallLetterException("The password should contain at least one small letter!");
        }
    }

    protected void validatePasswordForSpecialCharacters(String password) {
        if (!password.matches(REGEX_FOR_SPECIFIC_SYMBOL_1) && !password.matches(REGEX_FOR_SPECIFIC_SYMBOL_2)) {
            throw new PasswordSpecialCharacterException("The password should contain at least one special letter!");
        }
    }

    protected void validateExistingUser(String username, UserDatabase userDatabaseManager) {
        if (userDatabaseManager.getUserRecords().userDataMap().containsKey(username)) {
            throw new UserAlreadyExistsException("There is already such user in the system!");
        }
    }

    protected void validateSalt(byte[] passwordSalt) {
        if (passwordSalt == null) {
            throw new IllegalArgumentException("The salt password passed cannot be null!");
        }
    }

    protected void validateUserData(UserData userData) {
        if (userData == null) {
            throw new NoSuchElementException("There are no such user records in the database!");
        }
    }
}