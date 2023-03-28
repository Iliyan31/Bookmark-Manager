package bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.BookmarkBar;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Children;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.MetaInfo;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Other;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Roots;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Synced;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookmarkGroupsTest {
    static Bookmark bookmark;
    static Set<Bookmark> bookmarks;
    static BookmarkGroups bookmarkGroups;

    @BeforeAll
    static void setUp() {
        Set<String> tags = new HashSet<>();
        tags.add("tag");
        tags.add("tag2");
        bookmark = new Bookmark("anything", "null", "null", List.copyOf(tags));

        bookmarks = new HashSet<>();
        bookmarks.add(bookmark);

        Map<String, Collection<Bookmark>> groups = new HashMap<>();
        groups.put("group", bookmarks);
        bookmarkGroups = new BookmarkGroups(groups);
    }

    @Test
    void testCreationOfBookmarkGroups() {
        assertThrows(IllegalArgumentException.class, () -> new BookmarkGroups(null),
            "The system should correctly throw exception when creating bookmark groups with null value!");
    }

    @Test
    void testAddNewGroupWithNullGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.addNewGroup(null),
            "The system should correctly throw exception when adding bookmark group with null group name!");
    }

    @Test
    void testAddNewGroupWithEmptyGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.addNewGroup(""),
            "The system should correctly throw exception when adding bookmark group with empty group name!");
    }

    @Test
    void testAddNewGroupWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.addNewGroup(" "),
            "The system should correctly throw exception when adding bookmark group with blank group name!");
    }

    @Test
    void testAddNewGroupWithNoSuchGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.addNewGroup("group"),
            "The system should correctly throw exception when adding bookmark group with no such group name!");
    }

    @Test
    void testAddNewGroup() {
        bookmarkGroups.addNewGroup("group1");
        assertEquals(2, bookmarkGroups.getGroups().size(),
            "The system should correctly add new group!");
    }

    @Test
    void testAddBookmarkToGroupWithNullGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.addBookmarkToGroup(null, bookmark),
            "The system should correctly throw exception when adding bookmark with null group name!");
    }

    @Test
    void testAddBookmarkToGroupWithEmptyGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.addBookmarkToGroup("", bookmark),
            "The system should correctly throw exception when adding bookmark with empty group name!");
    }

    @Test
    void testAddBookmarkToGroupWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.addBookmarkToGroup(" ", bookmark),
            "The system should correctly throw exception when adding bookmark with blank group name!");
    }

    @Test
    void testAddBookmarkToGroupWithNullBookmark() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.addBookmarkToGroup("a", null),
            "The system should correctly throw exception when adding bookmark with null bookmark!");
    }

    @Test
    void testAddBookmarkToGroupWithNoSuchGroupName() {
        assertThrows(NoSuchElementException.class, () -> bookmarkGroups.addBookmarkToGroup("group2", bookmark),
            "The system should correctly throw exception when adding bookmark with no such group name!");
    }

    @Test
    void testAddBookmarkToGroup() {
        bookmarkGroups.addBookmarkToGroup("group", new Bookmark("a", "a", "a", new LinkedList<>()));
        assertEquals(2, bookmarkGroups.getAllUserBookmarks().size(),
            "The system should correctly add new bookmark!");
    }

    @Test
    void testRemoveBookmarkWithNullBookmark() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.removeBookmark(null),
            "The system should correctly throw exception when removing bookmark with null bookmark!");
    }

    @Test
    void testRemoveBookmark() {
        Bookmark bookmark1 = new Bookmark("a", "a", "a", new LinkedList<>());
        bookmarkGroups.addBookmarkToGroup("group", bookmark1);
        bookmarkGroups.removeBookmark(bookmark1);
        assertEquals(1, bookmarkGroups.getAllUserBookmarks().size(),
            "The system should correctly remove bookmarks!");
    }


    @Test
    void testRemoveBookmarkFromGroupWithNullGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.removeBookmarkFromGroup(null, "a"),
            "The system should correctly throw exception when remove bookmark from group with null group name!");
    }

    @Test
    void testRemoveBookmarkFromGroupWithEmptyGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.removeBookmarkFromGroup("", "a"),
            "The system should correctly throw exception when remove bookmark from group with empty group name!");
    }

    @Test
    void testRemoveBookmarkFromGroupWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.removeBookmarkFromGroup(" ", "a"),
            "The system should correctly throw exception when remove bookmark from group with blank group name!");
    }

    @Test
    void testRemoveBookmarkFromGroupWithNullBookmarkURL() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.removeBookmarkFromGroup("a", null),
            "The system should correctly throw exception when remove bookmark from group with null bookmark!");
    }

    @Test
    void testRemoveBookmarkFromGroupWithEmptyBookmarkURL() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.removeBookmarkFromGroup("a", ""),
            "The system should correctly throw exception when remove bookmark from group with null bookmark!");
    }

    @Test
    void testRemoveBookmarkFromGroupWithBlankBookmarkURL() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.removeBookmarkFromGroup("a", "    "),
            "The system should correctly throw exception when remove bookmark from group with null bookmark!");
    }

    @Test
    void testRemoveBookmarkFromGroupWithNoSuchGroupName() {
        assertThrows(NoSuchElementException.class, () -> bookmarkGroups.removeBookmarkFromGroup("group2", "bookmark"),
            "The system should correctly throw exception when remove bookmark from group with no such group name!");
    }

    @Test
    void testRemoveBookmarkFromGroup() {
        Bookmark bookmark1 = new Bookmark("a", "a", "a", new LinkedList<>());
        bookmarkGroups.addBookmarkToGroup("group", bookmark1);
        bookmarkGroups.removeBookmarkFromGroup("group", "a");
        assertEquals(1, bookmarkGroups.getAllUserBookmarks().size(),
            "The system should correctly remove bookmarks!");
    }

    @Test
    void testGetAllUserBookmarks() {
        assertEquals(2, bookmarkGroups.getAllUserBookmarks().size(),
            "The system should correctly get all bookmarks!");
    }

    @Test
    void testGetAllUserBookmarksFromGroupWithNullGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.getAllUserBookmarksFromGroup(null),
            "The system should correctly throw exception when get all user bookmarks from group with null group name!");
    }

    @Test
    void testGetAllUserBookmarksFromGroupWithEmptyGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.getAllUserBookmarksFromGroup(""),
            "The system should correctly throw exception when get all user bookmarks from group with empty group name!");
    }

    @Test
    void testGetAllUserBookmarksFromGroupWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.getAllUserBookmarksFromGroup(" "),
            "The system should correctly throw exception when get all user bookmarks from group with blank group name!");
    }

    @Test
    void testGetAllUserBookmarksFromGroupWithNoSuchGroupName() {
        assertThrows(NoSuchElementException.class, () -> bookmarkGroups.getAllUserBookmarksFromGroup("group2"),
            "The system should correctly throw exception when get all user bookmarks from group with no such group name!");
    }

    @Test
    void testGetAllUserBookmarksFromGroup() {
        assertEquals(2, bookmarkGroups.getAllUserBookmarksFromGroup("group").size(),
            "The system should correctly get all bookmarks from group!");
    }


    @Test
    void testGetAllUserBookmarksByTagsWithNullTags() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.getAllUserBookmarksByTags(null),
            "The system should correctly throw exception when passed null tag list!");
    }

    @Test
    void testGetAllUserBookmarksByTags() {
        List<String> tags = new LinkedList<>();
        tags.add("tag");
        tags.add("tag2");

        Bookmark bookmark1 = new Bookmark("a", "a", "a", tags);
        bookmarkGroups.addBookmarkToGroup("group", bookmark1);
        assertEquals(2, bookmarkGroups.getAllUserBookmarksByTags(tags).size(),
            "The system should correctly get all bookmarks by tags!");
    }

    @Test
    void testGetAllUserBookmarksByTitleNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.getAllUserBookmarksByTitle(null),
            "The system should correctly throw exception when passed null title!");
    }

    @Test
    void testGetAllUserBookmarksByTitleEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.getAllUserBookmarksByTitle(""),
            "The system should correctly throw exception when passed empty title!");
    }

    @Test
    void testGetAllUserBookmarksByTitleBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.getAllUserBookmarksByTitle(" "),
            "The system should correctly throw exception when passed blank title!");
    }

    @Test
    void testGetAllUserBookmarksByTitle() {
        assertEquals(1, bookmarkGroups.getAllUserBookmarksByTitle("a").size(),
            "The system should correctly get all bookmarks by title!");
    }

    @Test
    void testImportBookmarksFromChromeNullChromeBookmarks() {
        assertThrows(IllegalArgumentException.class, () -> bookmarkGroups.importBookmarksFromChrome(null),
            "The system should correctly throw exception when passed null chrome bookmarks!");
    }

    @Test
    void testImportBookmarksFromChrome() {
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

        bookmarkGroups.importBookmarksFromChrome(chromeBookmarks);
        assertEquals("a", bookmarkGroups.getChromeBookmarks().checksum(),
            "The system should correctly add chrome bookmarks!");
    }
}