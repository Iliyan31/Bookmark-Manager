package bg.sofia.uni.fmi.mjt.bookmarks.authentication.login;

import bg.sofia.uni.fmi.mjt.bookmarks.authentication.registration.Registration;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database.NoSuchUserException;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.login.WrongUserPasswordException;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginTest {
    static UserDatabaseManager userDatabaseManager;
    static SocketChannel channel;
    static Registration registration;

    @BeforeAll
    static void setUp() throws IOException {
        channel = SocketChannel.open();

        userDatabaseManager =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        registration = new Registration("HelloWorld1", "12345678Hi@", userDatabaseManager);
        registration.registerUser();
    }

    @Test
    void testLoginUserWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Login(null, "12345678Hi@", userDatabaseManager, channel),
            "The system should correctly throw exception when user logs with incorrect name!");
    }

    @Test
    void testLoginUserWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Login("", "12345678Hi@", userDatabaseManager, channel),
            "The system should correctly throw exception when user logs with incorrect name!");
    }

    @Test
    void testLoginUserWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Login(" ", "12345678Hi@", userDatabaseManager, channel),
            "The system should correctly throw exception when user logs with incorrect name!");
    }

    @Test
    void testLoginUserWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new Login("HelloWorld1", null, userDatabaseManager, channel),
            "The system should correctly throw exception when user logs with incorrect password!");
    }

    @Test
    void testLoginUserWithEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> new Login("HelloWorld1", "", userDatabaseManager, channel),
            "The system should correctly throw exception when user logs with incorrect password!");
    }

    @Test
    void testLoginUserWithBlankPassword() {
        assertThrows(IllegalArgumentException.class, () -> new Login("HelloWorld1", " ", userDatabaseManager, channel),
            "The system should correctly throw exception when user logs with incorrect password!");
    }

    @Test
    void testLoginUserWithNullUserDatabaseManager() {
        assertThrows(IllegalArgumentException.class, () -> new Login("HelloWorld1", "12345678Hi@", null, channel),
            "The system should correctly throw exception when user logs with null database manager!");
    }

    @Test
    void testLoginUserWithNullClientChannel() {
        assertThrows(IllegalArgumentException.class,
            () -> new Login("HelloWorld1", "12345678Hi@", userDatabaseManager, null),
            "The system should correctly throw exception when user logs with null client channel!");
    }

    @Test
    void testLoginUserWithNoSuchUser() {
        Login login = new Login("HelloWorld2", "12345678Hi@", userDatabaseManager, channel);
        assertThrows(NoSuchUserException.class, login::loginUser,
            "The system should correctly display error when there is no such user!");
    }

    @Test
    void testLoginUserWithWrongPassword() {
        Login login = new Login("HelloWorld1", "1234567Hi@", userDatabaseManager, channel);
        assertThrows(WrongUserPasswordException.class, login::loginUser,
            "The system should correctly display error when user tries to log with wrong password!");
    }

    @AfterAll
    static void cleanUp() throws IOException {
        channel.close();
    }
}