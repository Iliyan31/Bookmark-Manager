package bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome;

import com.google.gson.annotations.SerializedName;

public record MetaInfo(@SerializedName("power_bookmark_meta") String powerBookmarkMeta) {
}