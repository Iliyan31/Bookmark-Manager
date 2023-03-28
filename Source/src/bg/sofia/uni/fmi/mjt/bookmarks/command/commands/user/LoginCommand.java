package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.user;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class LoginCommand extends CommandsValidator implements Command {
    private static final int USERNAME_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private final String[] args;
    private final SocketChannel clientChannel;
    private final BookmarkManagerAPI bookmarkManager;

    public LoginCommand(String[] args, SocketChannel clientChannel, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.clientChannel = clientChannel;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            validateLoginArgumentsArray(args);
            validateLoginArguments(args);
            validateLoginNumberOfArguments(args);

            bookmarkManager.login(args[USERNAME_INDEX], args[PASSWORD_INDEX], clientChannel);
            return "The user has successfully been logged!";
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