package bg.sofia.uni.fmi.mjt.bookmarks.manager;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class OSManager {
    private static final String WINDOWS = "Windows";
    private static final String LINUX = "Linux";
    private static final String MAC = "MacOS";
    private static final String UNKNOWN = "Unknown";
    private final Gson gson;
    private final String os;
    private final String username;

    public OSManager() {
        this.gson = new Gson();
        this.os = System.getProperty("os.name");
        this.username = System.getProperty("user.name");
    }

    public ChromeBookmarks getChromeBookmarks() throws IOException {
        return gson.fromJson(Files.readString(Path.of(getPath()), StandardCharsets.UTF_8), ChromeBookmarks.class);
    }

    private String getPath() {
        if (os.contains(WINDOWS)) {
            return getWindowsPath(username);
        }
        if (os.contains(LINUX)) {
            return getLinuxPath();
        }
        if (os.contains(MAC)) {
            return getMacPath(username);
        }

        return UNKNOWN;
    }

    private String getWindowsPath(String username) {
        return String.format("C:\\Users\\%s\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks", username);
    }

    private String getMacPath(String username) {
        return String.format("/Users/%s/Library/Application\\ Support/Google/Chrome/Default/Bookmarks", username);
    }

    private String getLinuxPath() {
        return "~/.config/google-chrome/Default/Bookmarks";
    }
}