package bg.sofia.uni.fmi.mjt.bookmarks.repository;

import java.io.IOException;
import java.nio.file.Path;

public interface Database {
    void insertDataToDatabase(String allBookmarkData, Path path) throws IOException;

    String loadDataFromDatabase(Path path) throws IOException;

    boolean isDatabaseEmpty(Path path) throws IOException;
}