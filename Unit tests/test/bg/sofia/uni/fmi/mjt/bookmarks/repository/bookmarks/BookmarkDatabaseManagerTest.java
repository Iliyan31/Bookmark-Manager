package bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database.NoSuchUserException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookmarkDatabaseManagerTest {
    static File db;
    static BookmarkDatabase bookmarkDatabase;

    @BeforeAll
    static void setUp() throws IOException {
        db = File.createTempFile("test", ".txt");
        bookmarkDatabase = new BookmarkDatabaseManager(db.getPath());
        bookmarkDatabase.createNewGroupForUser("Iliyan", "Hi");
    }

    @Test
    void testCreateDBWithNullPath() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkDatabaseManager(null),
            "The system should correctly handle null path!");
    }

    @Test
    void testInsertAllUsersToDatabase() throws IOException {
        bookmarkDatabase.insertBookmarkDataToDatabase();
        assertTrue(db.length() > 0, "The database should correctly insert all data!");
    }

    @Test
    void testClearAllData() {
        bookmarkDatabase.clearAllData();
        assertThrows(NoSuchUserException.class, () -> bookmarkDatabase.getAllUserBookmarks("Iliyan"),
            "The database should correctly clear all data!");
    }

    @AfterAll
    static void cleanUp() throws IOException {
        Files.deleteIfExists(Path.of(db.getPath()));
    }
}
