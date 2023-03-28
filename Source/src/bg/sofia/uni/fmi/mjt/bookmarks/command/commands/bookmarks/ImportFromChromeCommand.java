package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ImportFromChromeCommand extends CommandsValidator implements Command {
    private final String[] args;
    private final SocketChannel clientChannel;
    private final BookmarkManagerAPI bookmarkManager;

    public ImportFromChromeCommand(String[] args, SocketChannel clientChannel, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.clientChannel = clientChannel;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            validateImportFromChromeArgumentsArray(args);
            validateImportFromChrome(args);

            bookmarkManager.importFromChrome(clientChannel);
            return "Successfully added bookmarks from Chrome browser!";
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