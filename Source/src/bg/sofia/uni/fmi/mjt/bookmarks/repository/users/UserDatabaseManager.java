package bg.sofia.uni.fmi.mjt.bookmarks.repository.users;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.user.UserData;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.user.UserRecords;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.DatabaseOperations;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.HashMap;

public class UserDatabaseManager extends DatabaseOperations implements UserDatabase, UserDatabaseManagerValidator {
    private final Gson gson;
    private final Path userDatabasePath;
    private final UserRecords userRecords;

    public UserDatabaseManager(String path) throws IOException {
        validateDatabasePath(path);

        this.gson = new Gson();
        this.userDatabasePath = Path.of(path);
        this.userRecords = loadUsersFromDatabase(userDatabasePath);
    }

    @Override
    public void insertUserRecord(String username, UserData userData) {
        userRecords.addUser(username, userData);
    }

    @Override
    public void loginUser(String username, SocketChannel clientChannel) {
        userRecords.loginUser(username, clientChannel);
    }

    @Override
    public void logoutUser(String username, SocketChannel clientChannel) {
        userRecords.logoutUser(username, clientChannel);
    }

    @Override
    public void insertAllUsersToDatabase() throws IOException {
        insertDataToDatabase(gson.toJson(userRecords), userDatabasePath);
    }

    @Override
    public UserRecords getUserRecords() {
        return userRecords;
    }

    @Override
    public UserData getSpecificUserData(String username) {
        validateForSuchUser(username, this);

        return userRecords.userDataMap().get(username);
    }

    @Override
    public String getUserBySocketChannel(SocketChannel clientChannel) {
        return userRecords.getUserBySocketChannel(clientChannel);
    }

    @Override
    public void logoutAllUsers() {
        userRecords.logoutAllUsers();
    }

    private UserRecords loadUsersFromDatabase(Path path) throws IOException {
        if (isDatabaseEmpty(path)) {
            return new UserRecords(new HashMap<>(), new HashMap<>(), new HashMap<>());
        }

        return new UserRecords(gson.fromJson(loadDataFromDatabase(path), UserRecords.class).userDataMap(),
            new HashMap<>(), new HashMap<>());
    }
}