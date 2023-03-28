package bg.sofia.uni.fmi.mjt.bookmarks.entities.bitly;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

public record BitlyResponse(@SerializedName("created_at") String createdAt, String id, String link,
                            @SerializedName("custom_bitlinks") Collection<String> customBitlinks,
                            @SerializedName("long_url") String longURL,
                            boolean archived, Collection<String> tags, Collection<Deeplink> deeplinks,
                            Reference references) {
}