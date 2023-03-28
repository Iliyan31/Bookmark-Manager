package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.user;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;

import java.io.IOException;

public class RegisterCommand extends CommandsValidator implements Command {
    private static final int USERNAME_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private final String[] args;
    private final BookmarkManagerAPI bookmarkManager;

    public RegisterCommand(String[] args, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            validateRegisterArgumentsArray(args);
            validateRegisterArguments(args);
            validateRegisterNumberOfArguments(args);

            bookmarkManager.register(args[USERNAME_INDEX], args[PASSWORD_INDEX]);
            return "Hi " + args[USERNAME_INDEX];
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