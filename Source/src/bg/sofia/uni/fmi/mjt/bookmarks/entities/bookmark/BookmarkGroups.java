package bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BookmarkGroups extends BookmarksValidator {
    private final Map<String, Collection<Bookmark>> groups;

    @SerializedName("chrome_bookmarks")
    private ChromeBookmarks chromeBookmarks;

    public BookmarkGroups(Map<String, Collection<Bookmark>> groups) {
        validateGroupsMap(groups);

        this.groups = groups;
        this.chromeBookmarks = new ChromeBookmarks("", null, "");
    }

    public void addNewGroup(String groupName) {
        validateGroupName(groupName);
        validateThatGroupDoesNotContainGroupName(groupName, groups);

        groups.put(groupName, new HashSet<>());
    }

    public void addBookmarkToGroup(String groupName, Bookmark bookmark) {
        validateGroupName(groupName);
        validateBookmark(bookmark);
        validateGroupContainGroupName(groupName, groups);

        groups.get(groupName).add(bookmark);
    }

    public void removeBookmark(Bookmark bookmark) {
        validateBookmark(bookmark);

        for (Collection<Bookmark> bookmarkSet : groups.values()) {
            bookmarkSet.remove(bookmark);
        }
    }

    public void removeBookmarkFromGroup(String groupName, String bookmarkURL) {
        validateGroupName(groupName);
        validateBookmarkURl(bookmarkURL);
        validateGroupContainGroupName(groupName, groups);

        groups.get(groupName).removeIf(bookmark ->
            bookmark.bookmarkURL().equals(bookmarkURL) || bookmark.shortenedURL().equals(bookmarkURL));
    }

    public Collection<Bookmark> getAllUserBookmarks() {
        Set<Bookmark> allBookmarks = new HashSet<>();

        for (Collection<Bookmark> bookmarks : groups.values()) {
            allBookmarks.addAll(bookmarks);
        }

        return allBookmarks;
    }

    public Collection<Bookmark> getAllUserBookmarksFromGroup(String groupName) {
        validateGroupName(groupName);
        validateGroupContainGroupName(groupName, groups);

        return groups.get(groupName);
    }

    public Collection<Bookmark> getAllUserBookmarksByTags(Collection<String> bookmarkTags) {
        validateTagsList(bookmarkTags);

        Set<Bookmark> bookmarksByTags = new HashSet<>();

        for (Bookmark bookmark : getAllUserBookmarks()) {
            boolean containsElement = bookmark.bookmarkTags().stream()
                .anyMatch(bookmarkTags::contains);

            if (containsElement) {
                bookmarksByTags.add(bookmark);
            }
        }

        return bookmarksByTags;
    }

    public Collection<Bookmark> getAllUserBookmarksByTitle(String bookmarkTitle) {
        validateTitle(bookmarkTitle);

        Set<Bookmark> bookmarksByTitle = new HashSet<>();

        for (Bookmark bookmark : getAllUserBookmarks()) {
            boolean containsElement = bookmark.bookmarkTitle().toLowerCase().contains(bookmarkTitle.toLowerCase());

            if (containsElement) {
                bookmarksByTitle.add(bookmark);
            }
        }

        return bookmarksByTitle;
    }

    public void importBookmarksFromChrome(ChromeBookmarks chromeBookmarks) {
        validateChromeBookmarks(chromeBookmarks);

        this.chromeBookmarks = chromeBookmarks;
    }

    public ChromeBookmarks getChromeBookmarks() {
        return chromeBookmarks;
    }

    public Map<String, Collection<Bookmark>> getGroups() {
        return groups;
    }
}