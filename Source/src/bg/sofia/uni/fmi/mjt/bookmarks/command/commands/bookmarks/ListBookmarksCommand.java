package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandLengths;
import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Collection;

public class ListBookmarksCommand extends CommandsValidator implements Command {
    private static final int GROUP_NAME_INDEX = 1;
    private final String[] args;
    private final SocketChannel clientChannel;
    private final BookmarkManagerAPI bookmarkManager;

    public ListBookmarksCommand(String[] args, SocketChannel clientChannel, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.clientChannel = clientChannel;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            validateListBookmarksArgumentsArray(args);
            validateListBookmarksArguments(args);
            validateListBookmarksNumberOfArguments(args);
            validateListBookmarksGroupNameArgument(args);

            if (args.length == CommandLengths.LIST_BOOKMARKS_ONLY.getCommandLengths()) {
                return convertToJson(bookmarkManager.listAllBookmarks(clientChannel));
            } else {
                return convertToJson(bookmarkManager.listAllBookmarks(args[GROUP_NAME_INDEX], clientChannel));
            }
        } catch (Exception e) {
            try {
                bookmarkManager.addLogs(e);
            } catch (IOException ioException) {
                return ioException.getMessage();
            }

            return e.getMessage();
        }
    }

    private String convertToJson(Collection<Bookmark> bookmarks) {
        Gson gson = new Gson();
        return gson.toJson(bookmarks);
    }
}