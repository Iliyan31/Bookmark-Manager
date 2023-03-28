package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.user;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class LogoutCommand extends CommandsValidator implements Command {
    private final String[] args;
    private final SocketChannel clientChannel;
    private final BookmarkManagerAPI bookmarkManager;

    public LogoutCommand(String[] args, SocketChannel clientChannel, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.clientChannel = clientChannel;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            validateLogoutArgumentsArray(args);
            validateLogoutArguments(args);
            validateLogoutNumberOfArguments(args);

            bookmarkManager.logout(clientChannel);
            return "The user has successfully been logged out!";
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