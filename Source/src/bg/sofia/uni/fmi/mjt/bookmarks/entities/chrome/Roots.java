package bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome;

import com.google.gson.annotations.SerializedName;

public record Roots(@SerializedName("bookmark_bar") BookmarkBar bookmarkBar, Other other, Synced synced) {
}