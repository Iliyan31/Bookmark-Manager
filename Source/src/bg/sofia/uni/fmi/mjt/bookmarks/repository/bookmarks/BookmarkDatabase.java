package bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;

import java.io.IOException;
import java.util.Collection;

public interface BookmarkDatabase {
    void insertBookmarkDataToDatabase() throws IOException;

    void createNewGroupForUser(String username, String groupName);

    void addBookmarkToUserGroup(String username, String groupName, Bookmark bookmark);

    void removeBookmarks(String username, Collection<Bookmark> bookmarks);

    void removeBookmarkFromGroup(String username, String groupName, String bookmarkURL);

    Collection<Bookmark> getAllUserBookmarks(String username);

    Collection<Bookmark> getAllUserBookmarksFromGroup(String username, String groupName);

    Collection<Bookmark> getBookmarksByTags(String username, Collection<String> tags);

    Collection<Bookmark> getBookmarksByTitle(String username, String title);

    void addChromeBookmarks(String username, ChromeBookmarks chromeBookmarks);

    ChromeBookmarks getChromeBookmarks(String username);

    void clearAllData();
}