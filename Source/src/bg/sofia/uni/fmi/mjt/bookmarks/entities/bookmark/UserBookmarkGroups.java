package bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserBookmarkGroups extends BookmarksValidator {
    @SerializedName("userGroups")
    private final Map<String, BookmarkGroups> groups;

    public UserBookmarkGroups(Map<String, BookmarkGroups> groupsPerUser) {
        validateGroupsPerUser(groupsPerUser);

        this.groups = groupsPerUser;
    }

    public void addGroupToUser(String username, String groupName) {
        validateUsername(username);
        validateGroupName(groupName);

        BookmarkGroups userGroups = new BookmarkGroups(new HashMap<>());
        userGroups.addNewGroup(groupName);

        if (!groups.containsKey(username)) {
            groups.put(username, userGroups);
        } else {
            groups.get(username).addNewGroup(groupName);
        }
    }

    public void addBookmarkToUserGroup(String username, String groupName, Bookmark bookmark) {
        validateUsername(username);
        validateGroupName(groupName);
        validateBookmark(bookmark);
        validateExistingUserInGroups(username, groups);

        groups.get(username).addBookmarkToGroup(groupName, bookmark);
    }

    public void removeBookmark(String username, Collection<Bookmark> bookmarks) {
        validateUsername(username);
        validateBookmarksSet(bookmarks);
        validateExistingUserInGroups(username, groups);

        for (Bookmark bookmark : bookmarks) {
            groups.get(username).removeBookmark(bookmark);
        }
    }

    public void removeBookMarkFromGroup(String username, String groupName, String bookmarkURL) {
        validateUsername(username);
        validateGroupName(groupName);
        validateBookmarkURl(bookmarkURL);
        validateExistingUserInGroups(username, groups);

        groups.get(username).removeBookmarkFromGroup(groupName, bookmarkURL);
    }

    public Collection<Bookmark> getAllUserBookmarks(String username) {
        validateUsername(username);
        validateExistingUserInGroups(username, groups);

        return groups.get(username).getAllUserBookmarks();
    }

    public Collection<Bookmark> getAllUserBookmarksFromGroup(String username, String groupName) {
        validateUsername(username);
        validateGroupName(groupName);
        validateExistingUserInGroups(username, groups);

        return groups.get(username).getAllUserBookmarksFromGroup(groupName);
    }

    public void clearAllData() {
        groups.clear();
    }

    public Collection<Bookmark> getBookmarksByTags(String username, Collection<String> tags) {
        validateUsername(username);
        validateTagsList(tags);
        validateTagsListArguments(tags);
        validateExistingUserInGroups(username, groups);

        return groups.get(username).getAllUserBookmarksByTags(tags);
    }

    public Collection<Bookmark> getBookmarksByTitle(String username, String title) {
        validateUsername(username);
        validateTitle(title);
        validateExistingUserInGroups(username, groups);

        return groups.get(username).getAllUserBookmarksByTitle(title);
    }

    public void addChromeBookmarks(String username, ChromeBookmarks chromeBookmarks) {
        validateUsername(username);
        validateChromeBookmarks(chromeBookmarks);
        validateExistingUserInGroups(username, groups);

        groups.get(username).importBookmarksFromChrome(chromeBookmarks);
    }

    public ChromeBookmarks getChromeBookmarks(String username) {
        validateUsername(username);
        validateExistingUserInGroups(username, groups);

        return groups.get(username).getChromeBookmarks();
    }
}