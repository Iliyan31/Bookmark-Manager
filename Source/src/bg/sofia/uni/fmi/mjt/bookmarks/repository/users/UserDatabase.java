package bg.sofia.uni.fmi.mjt.bookmarks.repository.users;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.user.UserData;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.user.UserRecords;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface UserDatabase {
    void insertUserRecord(String username, UserData userData);

    void loginUser(String username, SocketChannel clientChannel);

    void logoutUser(String username, SocketChannel clientChannel);

    void insertAllUsersToDatabase() throws IOException;

    UserRecords getUserRecords();

    UserData getSpecificUserData(String username);

    String getUserBySocketChannel(SocketChannel clientChannel);

    void logoutAllUsers();
}