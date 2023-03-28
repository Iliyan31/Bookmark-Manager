package bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark.UserBookmarkGroups;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.DatabaseOperations;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;

public class BookmarkDatabaseManager extends DatabaseOperations
    implements BookmarkDatabase, BookmarkDatabaseManagerValidator {
    private final Gson gson;
    private final Path bookmarkDatabasePath;
    private final UserBookmarkGroups userBookmarkGroups;

    public BookmarkDatabaseManager(String path) throws IOException {
        validateDatabasePath(path);

        this.gson = new Gson();
        this.bookmarkDatabasePath = Path.of(path);
        this.userBookmarkGroups = loadBookmarksFromDatabase(bookmarkDatabasePath);
    }

    @Override
    public void insertBookmarkDataToDatabase() throws IOException {
        insertDataToDatabase(gson.toJson(userBookmarkGroups), bookmarkDatabasePath);
    }

    @Override
    public void createNewGroupForUser(String username, String groupName) {
        userBookmarkGroups.addGroupToUser(username, groupName);
    }

    @Override
    public void addBookmarkToUserGroup(String username, String groupName, Bookmark bookmark) {
        userBookmarkGroups.addBookmarkToUserGroup(username, groupName, bookmark);
    }

    @Override
    public void removeBookmarks(String username, Collection<Bookmark> bookmarks) {
        userBookmarkGroups.removeBookmark(username, bookmarks);
    }

    @Override
    public void removeBookmarkFromGroup(String username, String groupName, String bookmarkURL) {
        userBookmarkGroups.removeBookMarkFromGroup(username, groupName, bookmarkURL);
    }

    @Override
    public Collection<Bookmark> getAllUserBookmarks(String username) {
        return userBookmarkGroups.getAllUserBookmarks(username);
    }

    @Override
    public Collection<Bookmark> getAllUserBookmarksFromGroup(String username, String groupName) {
        return userBookmarkGroups.getAllUserBookmarksFromGroup(username, groupName);
    }

    @Override
    public Collection<Bookmark> getBookmarksByTags(String username, Collection<String> tags) {
        return userBookmarkGroups.getBookmarksByTags(username, tags);
    }

    @Override
    public Collection<Bookmark> getBookmarksByTitle(String username, String title) {
        return userBookmarkGroups.getBookmarksByTitle(username, title);
    }

    @Override
    public void addChromeBookmarks(String username, ChromeBookmarks chromeBookmarks) {
        userBookmarkGroups.addChromeBookmarks(username, chromeBookmarks);
    }

    @Override
    public ChromeBookmarks getChromeBookmarks(String username) {
        return userBookmarkGroups.getChromeBookmarks(username);
    }

    @Override
    public void clearAllData() {
        userBookmarkGroups.clearAllData();
    }

    private UserBookmarkGroups loadBookmarksFromDatabase(Path path) throws IOException {
        if (isDatabaseEmpty(path)) {
            return new UserBookmarkGroups(new HashMap<>());
        }

        return gson.fromJson(loadDataFromDatabase(path), UserBookmarkGroups.class);
    }
}