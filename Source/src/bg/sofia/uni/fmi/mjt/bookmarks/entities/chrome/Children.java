package bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome;

import com.google.gson.annotations.SerializedName;

public record Children(@SerializedName("date_added") String dateAdded,
                       @SerializedName("date_last_used") String dateLastUsed, String guid, String id,
                       @SerializedName("meta_info") MetaInfo metaInfo, String name, String type, String url) {
}