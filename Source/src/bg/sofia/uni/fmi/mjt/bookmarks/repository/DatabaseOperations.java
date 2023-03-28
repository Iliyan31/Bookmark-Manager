package bg.sofia.uni.fmi.mjt.bookmarks.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class DatabaseOperations implements Database {
    @Override
    public void insertDataToDatabase(String allBookmarkData, Path path) throws IOException {
        Files.writeString(path, allBookmarkData, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public String loadDataFromDatabase(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    @Override
    public boolean isDatabaseEmpty(Path path) throws IOException {
        return Files.size(path) == 0;
    }
}