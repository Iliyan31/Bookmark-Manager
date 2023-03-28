package bg.sofia.uni.fmi.mjt.bookmarks.manager;

import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabaseManager;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.logs.ErrorLogs;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BookmarkManagerTest {
    static UserDatabase userDatabase;
    static BookmarkDatabase bookmarkDatabase;
    static ErrorLogs errorLogs;
    HttpClient client = HttpClient.newBuilder().build();

    String stopwords = "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt";
    String suffixes = "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt";

    @BeforeAll
    static void setUp() throws IOException {
        userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        errorLogs = new ErrorLogs("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/logs/logs.txt");
    }

    @Test
    void testCreationOfBookmarkWithNullUserDatabase() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkManager(null, null, null, null, null, null, null),
            "The system should correctly handle any null arguments passed to the constructor");
    }

    @Test
    void testCreationOfBookmarkWithNullBookmarkDatabase() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkManager(userDatabase, null,
                null, null, null, null, null),
            "The system should correctly handle any null arguments passed to the constructor");
    }

    @Test
    void testCreationOfBookmarkWithNullStopWordsPath() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkManager(userDatabase, bookmarkDatabase,
                null, null, null, null, null),
            "The system should correctly handle any null arguments passed to the constructor");
    }

    @Test
    void testCreationOfBookmarkWithNullSuffixesPath() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkManager(userDatabase, bookmarkDatabase,
                stopwords, null, null, null, null),
            "The system should correctly handle any null arguments passed to the constructor");
    }

    @Test
    void testCreationOfBookmarkWithNullErrorLogs() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkManager(userDatabase, bookmarkDatabase,
                stopwords, suffixes, null, null, null),
            "The system should correctly handle any null arguments passed to the constructor");
    }

    @Test
    void testCreationOfBookmarkWithNullHttpClient() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkManager(userDatabase, bookmarkDatabase,
                stopwords, suffixes, errorLogs, null, null),
            "The system should correctly handle any null arguments passed to the constructor");
    }

    @Test
    void testCreationOfBookmarkWithNullBitlyCommunicator() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkManager(userDatabase, bookmarkDatabase,
                stopwords, suffixes, errorLogs, client, null),
            "The system should correctly handle any null arguments passed to the constructor");
    }
}