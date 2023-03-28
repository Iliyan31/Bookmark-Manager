package bg.sofia.uni.fmi.mjt.bookmarks.manager;

import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.manager.UserNotLoggedException;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.logs.ErrorLogs;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;

import java.net.http.HttpClient;
import java.nio.channels.SocketChannel;

public abstract class BookmarkManagerValidator {
    void validateUserDatabase(UserDatabase userDatabaseManager) {
        if (userDatabaseManager == null) {
            throw new IllegalArgumentException("The user database manager cannot be null!");
        }
    }

    void validateBookmarkDatabase(BookmarkDatabase bookmarkDatabaseManager) {
        if (bookmarkDatabaseManager == null) {
            throw new IllegalArgumentException("The user bookmark database manager cannot be null!");
        }
    }

    void validateErrorLogs(ErrorLogs errorLogs) {
        if (errorLogs == null) {
            throw new IllegalArgumentException("The error logs cannot be null");
        }
    }

    void validateLoggedUser(UserDatabase userDatabaseManager, SocketChannel clientChannel) {
        if (!userDatabaseManager.getUserRecords().userChannels().containsKey(clientChannel)) {
            throw new UserNotLoggedException("The user should be logged in order to access the system!");
        }
    }

    void validateStopWordsPath(String stopWordsPath) {
        if (stopWordsPath == null || stopWordsPath.isEmpty() || stopWordsPath.isBlank()) {
            throw new IllegalArgumentException("The stop words path cannot be null, empty or blank!");
        }
    }

    void validateSuffixesPath(String suffixesPath) {
        if (suffixesPath == null || suffixesPath.isEmpty() || suffixesPath.isBlank()) {
            throw new IllegalArgumentException("The suffixes path cannot be null, empty or blank!");
        }
    }

    void validateHttpClient(HttpClient client) {
        if (client == null) {
            throw new IllegalArgumentException("The http client cannot be null!");
        }
    }
}