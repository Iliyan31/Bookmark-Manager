package bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookmarkTest {
    @Test
    void testCreationOfBookmarkWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Bookmark(null, "a", "a", new LinkedList<>()),
            "The system should correctly handle any null element in creation of bookmark!");
    }

    @Test
    void testCreationOfBookmarkWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Bookmark("", "a", "a", new LinkedList<>()),
            "The system should correctly handle any null element in creation of bookmark!");
    }

    @Test
    void testCreationOfBookmarkWithBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Bookmark(" ", "a", "a", new LinkedList<>()),
            "The system should correctly handle any null element in creation of bookmark!");
    }

    @Test
    void testCreationOfBookmarkWithNullURL() {
        assertThrows(IllegalArgumentException.class, () -> new Bookmark("null", null, "a", new LinkedList<>()),
            "The system should correctly handle any null element in creation of bookmark!");
    }

    @Test
    void testCreationOfBookmarkWithEmptyURL() {
        assertThrows(IllegalArgumentException.class, () -> new Bookmark("a", "", "a", new LinkedList<>()),
            "The system should correctly handle any null element in creation of bookmark!");
    }

    @Test
    void testCreationOfBookmarkWithBlankURL() {
        assertThrows(IllegalArgumentException.class, () -> new Bookmark("a", " ", "a", new LinkedList<>()),
            "The system should correctly handle any null element in creation of bookmark!");
    }

    @Test
    void testCreationOfBookmarkWithNullShortURL() {
        assertThrows(IllegalArgumentException.class, () -> new Bookmark("null", "null", null, new LinkedList<>()),
            "The system should correctly handle any null element in creation of bookmark!");
    }

    @Test
    void testCreationOfBookmarkWithNullTags() {
        assertThrows(IllegalArgumentException.class, () -> new Bookmark("null", "null", "null", null),
            "The system should correctly handle any null element in creation of bookmark!");
    }

    @Test
    void testCreationOfBookmarkWithAnyNullTags() {
        List<String> tags = new LinkedList<>();
        tags.add(null);
        assertThrows(IllegalArgumentException.class, () -> new Bookmark("null", "null", "null", tags),
            "The system should correctly handle any null element in creation of bookmark!");
    }


    @Test
    void testCreationOfBookmark() {
        List<String> tags = new LinkedList<>();
        tags.add("tag");
        Bookmark bookmark = new Bookmark("null", "null", "null", tags);
        assertEquals("null", bookmark.bookmarkURL(), "The system should correctly create bookmark!");
    }
}