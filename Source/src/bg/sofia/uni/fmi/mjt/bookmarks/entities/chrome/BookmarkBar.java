package bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

public record BookmarkBar(Collection<Children> children, @SerializedName("date_added") String dateAdded,
                          @SerializedName("date_last_used") String dateLastUsed,
                          @SerializedName("date_modified") String dateModified, String guid, String id, String name,
                          String type) {
}