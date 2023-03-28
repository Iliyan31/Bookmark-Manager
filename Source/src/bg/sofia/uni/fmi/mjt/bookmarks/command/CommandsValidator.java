package bg.sofia.uni.fmi.mjt.bookmarks.command;

public class CommandsValidator {
    private static final int GROUP_NAME_FROM_LENGTH = 2;
    private static final int SHORTEN_FROM_LENGTH = 1;

    static String validateClientInput(String clientInput) {
        if (clientInput == null || clientInput.isEmpty() || clientInput.isBlank()) {
            return "Unknown";
            //throw new IllegalArgumentException("The client input cannot be null, empty or blank!");
        }
        return clientInput;
    }

    protected void validateRegisterArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The register arguments cannot be null!");
        }
    }

    protected void validateRegisterArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException("The none of the register arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateRegisterNumberOfArguments(String[] args) {
        if (args.length != CommandLengths.REGISTER_ARGS_LEN.getCommandLengths()) {
            throw new IllegalArgumentException("The register user arguments cannot be different from two!");
        }
    }

    protected void validateLoginArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The login arguments cannot be null!");
        }
    }

    protected void validateLoginArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException("The none of the login arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateLoginNumberOfArguments(String[] args) {
        if (args.length != CommandLengths.LOGIN_ARGS_LEN.getCommandLengths()) {
            throw new IllegalArgumentException("The login user arguments cannot be different from two!");
        }
    }

    protected void validateLogoutArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The logout arguments cannot be null!");
        }
    }

    protected void validateLogoutArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException("The none of the logout arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateLogoutNumberOfArguments(String[] args) {
        if (args.length != CommandLengths.LOGOUT_ARGS_LEN.getCommandLengths()) {
            throw new IllegalArgumentException("The logout user arguments cannot be different from zero!");
        }
    }

    protected void validateCreateNewGroupArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The create new group arguments cannot be null!");
        }
    }

    protected void validateCreateNewGroupArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException(
                    "The none of the create new group arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateCreateNewGroupNumberOfArguments(String[] args) {
        if (args.length != CommandLengths.CREATE_NEW_GROUP_LEN_ARGS_LEN.getCommandLengths()) {
            throw new IllegalArgumentException("The create new group for user arguments cannot be different from one!");
        }
    }

    protected void validateAddBookmarkToGroupArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The add bookmark arguments cannot be null!");
        }
    }

    protected void validateAddBookmarkToGroupArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException("None of the add bookmark arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateAddBookmarkToGroupNumberOfArguments(String[] args) {
        if (args.length < CommandLengths.ADD_BOOKMARK_WITHOUT_SHORTEN.getCommandLengths() ||
            args.length > CommandLengths.ADD_BOOKMARK_WITH_SHORTEN.getCommandLengths()) {
            throw new IllegalArgumentException(
                "The add bookmark for user arguments cannot be less than two and more than three!");
        }
    }

    protected void validateAddBookmarkToGroupShortenArgument(String[] args) {
        if (args.length == CommandLengths.ADD_BOOKMARK_WITH_SHORTEN.getCommandLengths()) {
            int shortenIndex = CommandLengths.ADD_BOOKMARK_WITH_SHORTEN.getCommandLengths() - SHORTEN_FROM_LENGTH;
            if (!args[shortenIndex].equals(CommandType.SHORTEN.getCommandTypeString())) {
                throw new IllegalArgumentException(
                    "The add bookmark for user shorten argument must be exactly --shorten !");
            }
        }
    }

    protected void validateRemoveBookmarkFromGroupArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The remove bookmark from group arguments cannot be null!");
        }
    }

    protected void validateRemoveBookmarkFromGroupArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException(
                    "The none of the remove bookmark from group arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateRemoveBookmarkFromGroupNumberOfArguments(String[] args) {
        if (args.length != CommandLengths.REMOVE_FROM_GROUP.getCommandLengths()) {
            throw new IllegalArgumentException("The add bookmark for user arguments cannot different from two!");
        }
    }

    protected void validateListBookmarksArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The list bookmarks arguments cannot be null!");
        }
    }

    protected void validateListBookmarksArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException(
                    "The none of the list bookmarks arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateListBookmarksNumberOfArguments(String[] args) {
        if (args.length != CommandLengths.LIST_BOOKMARKS_ONLY.getCommandLengths() &&
            args.length != CommandLengths.LIST_BOOKMARKS_GROUP.getCommandLengths()) {
            throw new IllegalArgumentException(
                "The add bookmark for user arguments cannot be different from zero or two!");
        }
    }

    protected void validateListBookmarksGroupNameArgument(String[] args) {
        if (args.length == CommandLengths.LIST_BOOKMARKS_GROUP.getCommandLengths()) {
            int group = CommandLengths.LIST_BOOKMARKS_GROUP.getCommandLengths() - GROUP_NAME_FROM_LENGTH;
            if (!args[group].equals(CommandType.GROUP_NAME.getCommandTypeString())) {
                throw new IllegalArgumentException(
                    "The list bookmarks group name argument must be exactly --group-name !");
            }
        }
    }

    protected void validateCleanupArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The cleanup bookmarks arguments cannot be null!");
        }
    }

    protected void validateCleanupArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException(
                    "The none of the cleanup bookmarks arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateCleanupNumberOfArguments(String[] args) {
        if (args.length != CommandLengths.CLEANUP.getCommandLengths()) {
            throw new IllegalArgumentException("The cleanup bookmarks user arguments cannot be different from zero!");
        }
    }


    protected void validateSearchTagsArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The search tags arguments cannot be null!");
        }
    }

    protected void validateSearchTagsArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException(
                    "The none of the search tags arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateSearchTitleArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The search title arguments cannot be null!");
        }
    }

    protected void validateSearchTitleArguments(String[] args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.isBlank()) {
                throw new IllegalArgumentException(
                    "The none of the search title arguments can be null, empty or blank!");
            }
        }
    }

    protected void validateImportFromChromeArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The import from chrome arguments cannot be null!");
        }
    }

    protected void validateImportFromChrome(String[] args) {
        if (args.length != CommandLengths.IMPORT_FROM_CHROME.getCommandLengths()) {
            throw new IllegalArgumentException("Import from chrome should have zero arguments!");
        }
    }

    protected void validateGetChromeBookmarksArgumentsArray(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException("The import from chrome arguments cannot be null!");
        }
    }

    protected void validateGetChromeBookmarks(String[] args) {
        if (args.length != CommandLengths.GET_CHROME_BOOKMARKS.getCommandLengths()) {
            throw new IllegalArgumentException("Get from chrome should have zero arguments!");
        }
    }
}