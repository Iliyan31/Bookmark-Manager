package bg.sofia.uni.fmi.mjt.bookmarks.manager;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.SocketChannel;
import java.util.Collection;

public interface BookmarkManagerAPI {
    void register(String username, String password);

    void login(String username, String password, SocketChannel clientChannel);

    void logout(SocketChannel clientChannel);

    void createNewGroup(String groupName, SocketChannel clientChannel);

    void addBookmarkToGroup(String groupName, String bookmarkURL, SocketChannel clientChannel, boolean shorten)
        throws IOException, URISyntaxException, InterruptedException;

    void removeBookmarkFromGroup(String groupName, String bookmarkURL, SocketChannel clientChannel);

    Collection<Bookmark> listAllBookmarks(SocketChannel clientChannel);

    Collection<Bookmark> listAllBookmarks(String groupName, SocketChannel clientChannel);

    Collection<Bookmark> searchBookmarksByTags(Collection<String> tags, SocketChannel clientChannel);

    Collection<Bookmark> searchBookmarksByTitle(String title, SocketChannel clientChannel);

    void cleanupBookmarks(SocketChannel clientChannel) throws URISyntaxException, InterruptedException, IOException;

    void importFromChrome(SocketChannel clientChannel) throws IOException;

    ChromeBookmarks getChromeBookmarks(SocketChannel clientChannel);

    void addLogs(Exception e) throws IOException;
}