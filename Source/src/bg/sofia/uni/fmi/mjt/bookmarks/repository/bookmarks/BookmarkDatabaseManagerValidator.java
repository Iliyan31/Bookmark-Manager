package bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks;

public interface BookmarkDatabaseManagerValidator {
    default void validateDatabasePath(String path) {
        if (path == null || path.isEmpty() || path.isBlank()) {
            throw new IllegalArgumentException("The user database path cannot be null, empty or blank!");
        }
    }
}