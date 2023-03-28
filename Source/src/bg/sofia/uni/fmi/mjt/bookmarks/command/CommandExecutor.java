package bg.sofia.uni.fmi.mjt.bookmarks.command;

import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks.AddBookmarkCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks.CleanupCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks.CreateNewGroupCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks.GetChromeBookmarks;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks.ImportFromChromeCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks.ListBookmarksCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks.RemoveBookmarkCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks.SearchBookmarksCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.user.LoginCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.user.LogoutCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.user.RegisterCommand;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManager;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;

import java.nio.channels.SocketChannel;

public class CommandExecutor extends CommandsValidator {
    private final BookmarkManagerAPI bookmarkManager;

    public CommandExecutor(BookmarkManager bookmarkManager) {
        this.bookmarkManager = bookmarkManager;
    }

    public String execute(Command cmd, SocketChannel clientChannel) {
        CommandType commandType = CommandType.getCommandType(cmd.command());

        return switch (commandType) {
            case REGISTER -> new RegisterCommand(cmd.arguments(), bookmarkManager).execute();
            case LOGIN -> new LoginCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case LOGOUT -> new LogoutCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case NEW_GROUP -> new CreateNewGroupCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case ADD_TO -> new AddBookmarkCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case REMOVE_FROM -> new RemoveBookmarkCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case LIST -> new ListBookmarksCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case SEARCH -> new SearchBookmarksCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case CLEANUP -> new CleanupCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case IMPORT_FROM_CHROME ->
                new ImportFromChromeCommand(cmd.arguments(), clientChannel, bookmarkManager).execute();
            case GET_CHROME_BOOKMARKS ->
                new GetChromeBookmarks(cmd.arguments(), clientChannel, bookmarkManager).execute();
            default -> "Unknown command";
        };
    }
}