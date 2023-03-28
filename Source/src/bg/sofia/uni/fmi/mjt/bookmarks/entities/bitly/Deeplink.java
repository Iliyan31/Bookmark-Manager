package bg.sofia.uni.fmi.mjt.bookmarks.entities.bitly;

import com.google.gson.annotations.SerializedName;

public record Deeplink(String guid, String bitlink,
                       @SerializedName("app_uri_path") String appUriPath,
                       @SerializedName("install_url") String installUrl,
                       @SerializedName("app_guid") String appGuid, String os,
                       @SerializedName("install_type") String installType, String created, String modified,
                       @SerializedName("brand_guid") String brandGuid) {
}