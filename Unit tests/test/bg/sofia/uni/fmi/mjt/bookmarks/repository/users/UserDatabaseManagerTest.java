package bg.sofia.uni.fmi.mjt.bookmarks.repository.users;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.user.UserData;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database.NoSuchUserException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDatabaseManagerTest {
    static File db;
    static UserDatabase userDatabase;
    static SocketChannel socketChannel;

    @BeforeAll
    static void setUp() throws IOException {
        db = File.createTempFile("test", ".txt");
        userDatabase = new UserDatabaseManager(db.getPath());
        socketChannel = SocketChannel.open();
        userDatabase.insertUserRecord("Iliyan", new UserData("s", "s", new byte[] {-1}, "s"));
        userDatabase.loginUser("Iliyan", SocketChannel.open());
    }

    @Test
    void testCreateDBWithNullPath() {
        assertThrows(IllegalArgumentException.class, () -> new UserDatabaseManager(null),
            "The system should correctly handle null path!");
    }

    @Test
    void testInsertAllUsersToDatabase() throws IOException {
        userDatabase.insertAllUsersToDatabase();
        assertTrue(db.length() > 0, "The user database should correctly insert all data!");
    }

    @Test
    void testLogoutData() {
        userDatabase.logoutAllUsers();
        assertThrows(NoSuchUserException.class, () -> userDatabase.getUserBySocketChannel(socketChannel),
            "The user database should correctly logout all data!");
    }

    @AfterAll
    static void cleanUp() throws IOException {
        Files.deleteIfExists(Path.of(db.getPath()));
    }
}
