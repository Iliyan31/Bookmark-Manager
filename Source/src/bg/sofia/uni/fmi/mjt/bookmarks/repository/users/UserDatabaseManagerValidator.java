package bg.sofia.uni.fmi.mjt.bookmarks.repository.users;

import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database.NoSuchUserException;

public interface UserDatabaseManagerValidator {
    default void validateDatabasePath(String path) {
        if (path == null || path.isEmpty() || path.isBlank()) {
            throw new IllegalArgumentException("The user database path cannot be null, empty or blank!");
        }
    }

    default void validateForSuchUser(String username, UserDatabaseManager userDatabaseManager) {
        if (!userDatabaseManager.getUserRecords().userDataMap().containsKey(username)) {
            throw new NoSuchUserException("There is no such user in the database!");
        }
    }
}