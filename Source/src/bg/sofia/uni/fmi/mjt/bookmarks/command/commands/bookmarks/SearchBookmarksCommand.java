package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandType;
import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SearchBookmarksCommand extends CommandsValidator implements Command {
    private static final int SEARCH_TYPE_INDEX = 0;
    private static final int FIRST_WORD_TITLE_INDEX = 1;
    private final String[] args;
    private final SocketChannel clientChannel;
    private final BookmarkManagerAPI bookmarkManager;

    public SearchBookmarksCommand(String[] args, SocketChannel clientChannel, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.clientChannel = clientChannel;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            if (args[SEARCH_TYPE_INDEX].equals(CommandType.TAGS.getCommandTypeString())) {
                return searchBookmarksByTags(args, clientChannel);
            } else if (args[SEARCH_TYPE_INDEX].equals(CommandType.TITLE.getCommandTypeString())) {
                return searchBookmarksByTitle(args, clientChannel);
            }

            return "Invalid search type argument passed!";
        } catch (Exception e) {
            try {
                bookmarkManager.addLogs(e);
            } catch (IOException ioException) {
                return ioException.getMessage();
            }

            return e.getMessage();
        }
    }

    private String searchBookmarksByTags(String[] args, SocketChannel clientChannel) {
        validateSearchTagsArgumentsArray(args);
        validateSearchTagsArguments(args);

        List<String> tags = new LinkedList<>(List.of(args));
        tags.remove(0);
        return convertToJson(bookmarkManager.searchBookmarksByTags(tags, clientChannel));
    }

    private String searchBookmarksByTitle(String[] args, SocketChannel clientChannel) {
        validateSearchTitleArgumentsArray(args);
        validateSearchTitleArguments(args);

        return convertToJson(bookmarkManager.searchBookmarksByTitle(createTitle(args), clientChannel));
    }

    private String createTitle(String[] args) {
        StringBuilder title = new StringBuilder();

        for (int i = FIRST_WORD_TITLE_INDEX; i < args.length; i++) {
            title.append(args[i]);

            if (i != args.length - 1) {
                title.append(" ");
            }
        }

        return title.toString();
    }

    private String convertToJson(Collection<Bookmark> bookmarks) {
        Gson gson = new Gson();
        return gson.toJson(bookmarks);
    }
}