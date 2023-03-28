package bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

public record Bookmark(@SerializedName("title") String bookmarkTitle,
                       @SerializedName("url") String bookmarkURL,
                       @SerializedName("short_url") String shortenedURL,
                       @SerializedName("tags") Collection<String> bookmarkTags) {

    public Bookmark {
        if (bookmarkTitle == null || bookmarkTitle.isEmpty() || bookmarkTitle.isBlank()) {
            throw new IllegalArgumentException("The bookmark title cannot be null, empty or blank!");
        }

        if (bookmarkURL == null || bookmarkURL.isEmpty() || bookmarkURL.isBlank()) {
            throw new IllegalArgumentException("The bookmark URL cannot be null, empty or blank!");
        }

        if (shortenedURL == null) {
            throw new IllegalArgumentException("The shortened URL cannot be null!");
        }

        if (bookmarkTags == null) {
            throw new IllegalArgumentException("The bookmark tags list cannot be null!");
        }

        for (String tag : bookmarkTags) {
            if (tag == null) {
                throw new IllegalArgumentException("None of the tags can be null!");
            }
        }
    }
}