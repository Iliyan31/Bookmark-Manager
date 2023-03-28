package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class CleanupCommand extends CommandsValidator implements Command {
    private final String[] args;
    private final SocketChannel clientChannel;
    private final BookmarkManagerAPI bookmarkManager;

    public CleanupCommand(String[] args, SocketChannel clientChannel, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.clientChannel = clientChannel;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            validateCleanupArgumentsArray(args);
            validateCleanupArguments(args);
            validateCleanupNumberOfArguments(args);

            bookmarkManager.cleanupBookmarks(clientChannel);
            return "Successfully removed invalid bookmarks!";
        } catch (Exception e) {
            try {
                bookmarkManager.addLogs(e);
            } catch (IOException ioException) {
                return ioException.getMessage();
            }

            return e.getMessage();
        }
    }
}