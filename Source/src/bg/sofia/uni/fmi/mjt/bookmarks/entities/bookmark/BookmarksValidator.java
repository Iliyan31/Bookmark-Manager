package bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database.NoSuchUserException;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class BookmarksValidator {
    void validateGroupsMap(Map<String, Collection<Bookmark>> groups) {
        if (groups == null) {
            throw new IllegalArgumentException("The user groups cannot be null!");
        }
    }

    void validateGroupName(String groupName) {
        if (groupName == null || groupName.isEmpty() || groupName.isBlank()) {
            throw new IllegalArgumentException("The group name cannot be null, empty or blank!");
        }
    }

    void validateBookmark(Bookmark bookmark) {
        if (bookmark == null) {
            throw new IllegalArgumentException("The bookmark passed cannot be null, empty or blank!");
        }
    }

    void validateBookmarkURl(String bookmarkURL) {
        if (bookmarkURL == null || bookmarkURL.isEmpty() || bookmarkURL.isBlank()) {
            throw new IllegalArgumentException("The bookmark URL cannot be null, empty or blank!");
        }
    }

    void validateGroupsPerUser(Map<String, BookmarkGroups> groupsPerUser) {
        if (groupsPerUser == null) {
            throw new IllegalArgumentException("The groups per user map passed cannot be null!");
        }
    }

    void validateUsername(String username) {
        if (username == null || username.isEmpty() || username.isBlank()) {
            throw new IllegalArgumentException("The username passed cannot be null, empty or blank!");
        }
    }

    void validateBookmarksSet(Collection<Bookmark> bookmarks) {
        if (bookmarks == null) {
            throw new IllegalArgumentException("The bookmarks set passed cannot be null!");
        }
    }

    void validateTitle(String title) {
        if (title == null || title.isEmpty() || title.isBlank()) {
            throw new IllegalArgumentException("The title passed cannot be null, empty or blank!");
        }
    }

    void validateTagsList(Collection<String> tags) {
        if (tags == null) {
            throw new IllegalArgumentException("The tags list passed cannot be null!");
        }
    }

    void validateTagsListArguments(Collection<String> tags) {
        for (String tag : tags) {
            if (tag == null || tag.isEmpty() || tag.isBlank()) {
                throw new IllegalArgumentException("None of the tags passed can be null, empty or blank!");
            }
        }
    }

    void validateChromeBookmarks(ChromeBookmarks chromeBookmarks) {
        if (chromeBookmarks == null) {
            throw new IllegalArgumentException("The chrome bookmarks passed cannot be null!");
        }
    }

    void validateThatGroupDoesNotContainGroupName(String groupName, Map<String, Collection<Bookmark>> groups) {
        if (groups.containsKey(groupName)) {
            throw new IllegalArgumentException("There is already such group for the user!");
        }
    }

    void validateGroupContainGroupName(String groupName, Map<String, Collection<Bookmark>> groups) {
        if (!groups.containsKey(groupName)) {
            throw new NoSuchElementException("There is no such group!");
        }
    }

    void validateExistingUserInGroups(String username, Map<String, BookmarkGroups> groups) {
        if (!groups.containsKey(username)) {
            throw new NoSuchUserException("There is no such user in the database" +
                " or the user has not created any groups to list bookmarks!");
        }
    }
}