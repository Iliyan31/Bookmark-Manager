package bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.BookmarkBar;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Children;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.MetaInfo;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Other;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Roots;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Synced;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database.NoSuchUserException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserBookmarkGroupsTest {

    static Bookmark bookmark;
    static Collection<Bookmark> bookmarks;
    static BookmarkGroups bookmarkGroups;
    static UserBookmarkGroups userBookmarkGroups;
    static Set<String> tags;

    @BeforeAll
    static void setUp() {
        tags = new HashSet<>();
        tags.add("tag");
        tags.add("tag2");
        bookmark = new Bookmark("anything", "null", "null", tags);

        bookmarks = new HashSet<>();
        bookmarks.add(bookmark);

        Map<String, Collection<Bookmark>> groups = new HashMap<>();
        groups.put("group", bookmarks);
        bookmarkGroups = new BookmarkGroups(groups);

        Map<String, BookmarkGroups> groupsPerUser = new HashMap<>();
        groupsPerUser.put("Iliyan", bookmarkGroups);

        userBookmarkGroups = new UserBookmarkGroups(groupsPerUser);
    }

    @Test
    void testCreateUserGroupsWithNullConstructorValue() {
        assertThrows(IllegalArgumentException.class, () -> new UserBookmarkGroups(null),
            "The system should correctly throw exception when null value is passed to the constructor!");
    }

    @Test
    void testAddGroupToUserWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addGroupToUser(null, "a"),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testAddGroupToUserWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addGroupToUser("", "a"),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testAddGroupToUserWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addGroupToUser(" ", "a"),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testAddGroupToUserWithNullGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addGroupToUser("a", null),
            "The system should correctly throw exception when null group name is passed!");
    }

    @Test
    void testAddGroupToUserWithEmptyGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addGroupToUser("a", ""),
            "The system should correctly throw exception when empty group name is passed!");
    }

    @Test
    void testAddGroupToUserWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addGroupToUser("a", " "),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testAddBookmarkToUserGroupWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addBookmarkToUserGroup(null, "a", null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testAddBookmarkToUserGroupWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addBookmarkToUserGroup("", "a", null),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testAddBookmarkToUserGroupWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addBookmarkToUserGroup(" ", "a", null),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testAddBookmarkToUserGroupWithNullGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addBookmarkToUserGroup("a", null, null),
            "The system should correctly throw exception when null group name is passed!");
    }

    @Test
    void testAddBookmarkToUserGroupWithEmptyGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addBookmarkToUserGroup("a", "", null),
            "The system should correctly throw exception when empty group name is passed!");
    }

    @Test
    void testAddBookmarkToUserGroupWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addBookmarkToUserGroup("a", " ", null),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testAddBookmarkToUserGroupWithNullBookmark() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addBookmarkToUserGroup("a", "s", null),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testAddBookmarkToUserGroupWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.addBookmarkToUserGroup("a", "s", bookmark),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testRemoveBookmarkWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookmark(null, null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testRemoveBookmarkWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookmark("", null),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testRemoveBookmarkWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookmark(" ", null),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testRemoveBookmarkWithNullBookmark() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookmark("a", null),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testRemoveBookmarkWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.removeBookmark("a", bookmarks),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup(null, "a", null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup("", "a", null),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup(" ", "a", null),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithNullGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup("a", null, null),
            "The system should correctly throw exception when null group name is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithEmptyGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup("a", "", null),
            "The system should correctly throw exception when empty group name is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup("a", " ", null),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithNullBookmarkURL() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup("a", "a", null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithEmptyBookmarkURL() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup("a", "a", null),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithBlankBookmarkURL() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.removeBookMarkFromGroup("a", "a", null),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testRemoveBookMarkFromGroupWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.removeBookMarkFromGroup("a", "a", "a"),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testGetAllUserBookmarksWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarks(null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testGetAllUserBookmarksWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarks(""),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testGetAllUserBookmarksWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarks(" "),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testGetAllUserBookmarksWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.getAllUserBookmarks("a"),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testGetAllUserBookmarksFromGroupWithNullGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarksFromGroup("a", null),
            "The system should correctly throw exception when empty group name is passed!");
    }

    @Test
    void testGetAllUserBookmarksFromGroupWithEmptyGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarksFromGroup("a", ""),
            "The system should correctly throw exception when empty group name is passed!");
    }

    @Test
    void testGetAllUserBookmarksFromGroupWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarksFromGroup("a", " "),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testGetAllUserBookmarksFromGroupWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.getAllUserBookmarksFromGroup("a", "a"),
            "The system should correctly throw exception when blank group name is passed!");
    }


    @Test
    void testGetBookmarksByTagsWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getBookmarksByTags(null, null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testGetBookmarksByTagsWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getBookmarksByTags("", null),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testGetBookmarksByTagsWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getBookmarksByTags(" ", null),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testGetBookmarksByTagsWithNullTagCollection() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getBookmarksByTags("s", null),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testGetBookmarksByTagsWithNullTagInCollection() {
        Set<String> tags1 = new HashSet<>();
        tags1.add("tag");
        tags1.add(null);
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getBookmarksByTags("s", tags1),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testGetBookmarksByTagsWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.getBookmarksByTags("a", tags),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testGetBookmarksByTitleWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.getBookmarksByTitle("a", "a"),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testGetBookmarksByTitleWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarksFromGroup("a", null),
            "The system should correctly throw exception when empty group name is passed!");
    }

    @Test
    void testGetBookmarksByTitleWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarksFromGroup("a", ""),
            "The system should correctly throw exception when empty group name is passed!");
    }

    @Test
    void testGetBookmarksByTitleWithBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getAllUserBookmarksFromGroup("a", " "),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testAddChromeBookmarksWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addChromeBookmarks(null, null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testAddChromeBookmarksWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addChromeBookmarks("", null),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testAddChromeBookmarksWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addChromeBookmarks(" ", null),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testAddChromeBookmarksWithNoSuchUser() {
        MetaInfo metaInfo = new MetaInfo("");
        Children children = new Children("", "", "", "", metaInfo, "", "", "");
        List<Children> childrenList = new ArrayList<>();
        childrenList.add(children);
        ChromeBookmarks chromeBookmarks =
            new ChromeBookmarks("a",
                new Roots(new BookmarkBar(childrenList, "", "", "", "", "", "", ""),
                    new Other(new ArrayList<>(), "", "", "", "", "", "", ""),
                    new Synced(new ArrayList<>(), "", "", "", "", "", "", "")),
                "a");
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.addChromeBookmarks("a", chromeBookmarks),
            "The system should correctly throw exception when blank group name is passed!");
    }

    @Test
    void testAddChromeBookmarksWithNullChromeBookmark() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.addChromeBookmarks("s", null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testGetChromeBookmarksWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getChromeBookmarks(null),
            "The system should correctly throw exception when null username is passed!");
    }

    @Test
    void testGetChromeBookmarksWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getChromeBookmarks(""),
            "The system should correctly throw exception when empty username is passed!");
    }

    @Test
    void testGetChromeBookmarksWithBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userBookmarkGroups.getChromeBookmarks(" "),
            "The system should correctly throw exception when blank username is passed!");
    }

    @Test
    void testGetChromeBookmarksWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userBookmarkGroups.getChromeBookmarks("a"),
            "The system should correctly throw exception when blank group name is passed!");
    }
}