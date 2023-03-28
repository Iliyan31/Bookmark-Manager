package bg.sofia.uni.fmi.mjt.bookmarks.authentication.registration;

import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordCapitalLetterException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordLengthException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordNumberException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordSmallLetterException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.PasswordSpecialCharacterException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.password.UserAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegistrationTest {
    static UserDatabaseManager userDatabaseManager;
    static Registration registration;

    @BeforeAll
    static void setUp() throws IOException {
        userDatabaseManager =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
    }

    @Test
    void testCreateRegistrationWithNullUsername() {
        assertThrows(IllegalArgumentException.class,
            () -> new Registration(null, "123345678Hi!", userDatabaseManager),
            "The username cannot be null!");
    }

    @Test
    void testCreateRegistrationWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class,
            () -> new Registration("", "123345678Hi!", userDatabaseManager),
            "The username cannot be empty!");
    }

    @Test
    void testCreateRegistrationWithBlankUsername() {
        assertThrows(IllegalArgumentException.class,
            () -> new Registration(" ", "123345678Hi!", userDatabaseManager),
            "The username cannot be blank!");
    }

    @Test
    void testCreateRegistrationWithNullPassword() {
        assertThrows(IllegalArgumentException.class,
            () -> new Registration("iliyan", null, userDatabaseManager),
            "The password cannot be null!");
    }

    @Test
    void testCreateRegistrationWithEmptyPassword() {
        assertThrows(IllegalArgumentException.class,
            () -> new Registration("iliyan", "", userDatabaseManager),
            "The password cannot be empty!");
    }

    @Test
    void testCreateRegistrationWithBlankPassword() {
        assertThrows(IllegalArgumentException.class,
            () -> new Registration("iliyan", " ", userDatabaseManager),
            "The password cannot be blank!");
    }

    @Test
    void testCreateRegistrationWithPasswordLessThanEightSymbols() {
        assertThrows(PasswordLengthException.class,
            () -> new Registration("iliyan", "1234567", userDatabaseManager),
            "The password length cannot be below eight symbols!");
    }

    @Test
    void testCreateRegistrationWithPasswordWithoutNumber() {
        assertThrows(PasswordNumberException.class,
            () -> new Registration("iliyan", "asdfghjk", userDatabaseManager),
            "The password must have at least one number!");
    }

    @Test
    void testCreateRegistrationWithPasswordCapitalLetter() {
        assertThrows(PasswordCapitalLetterException.class,
            () -> new Registration("iliyan", "asdfgh12", userDatabaseManager),
            "The password must have at least one capital letter!");
    }

    @Test
    void testCreateRegistrationWithPasswordSmallLetter() {
        assertThrows(PasswordSmallLetterException.class,
            () -> new Registration("iliyan", "ASDFGH12", userDatabaseManager),
            "The password must have at least one small letter!");
    }

    @Test
    void testCreateRegistrationWithPasswordSpecialCharacter() {
        assertThrows(PasswordSpecialCharacterException.class,
            () -> new Registration("iliyan", "aASDFGH12", userDatabaseManager),
            "The password must have at least one special character!");
    }


    @Test
    void testCreateRegistrationOneUser() {
        registration = new Registration("HelloWorld", "12345678Hi@", userDatabaseManager);
        registration.registerUser();
        assertEquals(userDatabaseManager.getSpecificUserData("HelloWorld").username(), "HelloWorld",
            "The system should correctly register the user!");
    }

    @Test
    void testCreateRegistrationMoreUsers() {
        registration = new Registration("HelloWorld1", "12345678Hi@", userDatabaseManager);
        registration.registerUser();
        registration = new Registration("HelloWorld2", "12345678Hi@", userDatabaseManager);
        registration.registerUser();
        assertEquals(userDatabaseManager.getSpecificUserData("HelloWorld2").username(), "HelloWorld2",
            "The system should correctly register the user!");
    }

    @Test
    void testCreateRegistrationExistingUser() {
        registration = new Registration("HelloWorld3", "12345678Hi@", userDatabaseManager);
        registration.registerUser();
        assertThrows(UserAlreadyExistsException.class,
            () -> new Registration("HelloWorld3", "12345678Hi@", userDatabaseManager),
            "The system should correctly throw exception when there is already such user registered!");
    }
}
