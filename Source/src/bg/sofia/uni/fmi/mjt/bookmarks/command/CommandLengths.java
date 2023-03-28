package bg.sofia.uni.fmi.mjt.bookmarks.command;

public enum CommandLengths {
    REGISTER_ARGS_LEN(2),
    LOGIN_ARGS_LEN(2),
    LOGOUT_ARGS_LEN(0),
    CREATE_NEW_GROUP_LEN_ARGS_LEN(1),
    ADD_BOOKMARK_WITHOUT_SHORTEN(2),
    ADD_BOOKMARK_WITH_SHORTEN(3),
    REMOVE_FROM_GROUP(2),
    LIST_BOOKMARKS_ONLY(0),
    LIST_BOOKMARKS_GROUP(2),
    CLEANUP(0),
    IMPORT_FROM_CHROME(0),
    GET_CHROME_BOOKMARKS(0);

    private final int commandLengths;

    CommandLengths(int commandLengths) {
        this.commandLengths = commandLengths;
    }

    public int getCommandLengths() {
        return commandLengths;
    }
}