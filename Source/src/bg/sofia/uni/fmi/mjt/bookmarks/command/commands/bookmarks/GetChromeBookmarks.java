package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class GetChromeBookmarks extends CommandsValidator implements Command {
    private final String[] args;
    private final SocketChannel clientChannel;
    private final BookmarkManagerAPI bookmarkManager;

    public GetChromeBookmarks(String[] args, SocketChannel clientChannel, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.clientChannel = clientChannel;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            validateGetChromeBookmarksArgumentsArray(args);
            validateGetChromeBookmarks(args);

            return convertToJson(bookmarkManager.getChromeBookmarks(clientChannel));
        } catch (Exception e) {
            try {
                bookmarkManager.addLogs(e);
            } catch (IOException ioException) {
                return ioException.getMessage();
            }

            return e.getMessage();
        }
    }

    private String convertToJson(ChromeBookmarks chromeBookmarks) {
        Gson gson = new Gson();
        return gson.toJson(chromeBookmarks);
    }
}