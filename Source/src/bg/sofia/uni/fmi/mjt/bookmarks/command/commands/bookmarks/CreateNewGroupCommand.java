package bg.sofia.uni.fmi.mjt.bookmarks.command.commands.bookmarks;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandsValidator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.commands.Command;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManagerAPI;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class CreateNewGroupCommand extends CommandsValidator implements Command {
    private static final int GROUP_NAME_INDEX = 0;
    private final String[] args;
    private final SocketChannel clientChannel;
    private final BookmarkManagerAPI bookmarkManager;

    public CreateNewGroupCommand(String[] args, SocketChannel clientChannel, BookmarkManagerAPI bookmarkManager) {
        this.args = args;
        this.clientChannel = clientChannel;
        this.bookmarkManager = bookmarkManager;
    }

    @Override
    public String execute() {
        try {
            validateCreateNewGroupArgumentsArray(args);
            validateCreateNewGroupArguments(args);
            validateCreateNewGroupNumberOfArguments(args);

            bookmarkManager.createNewGroup(args[GROUP_NAME_INDEX], clientChannel);
            return "The group was successfully added";
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