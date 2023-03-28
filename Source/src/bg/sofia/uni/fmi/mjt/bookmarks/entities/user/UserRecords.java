package bg.sofia.uni.fmi.mjt.bookmarks.entities.user;

import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database.NoSuchUserException;
import com.google.gson.annotations.SerializedName;

import java.nio.channels.SocketChannel;
import java.util.Map;

public class UserRecords extends UserRecordsValidator {
    @SerializedName("users")
    private Map<String, UserData> userDataMap;
    private final transient Map<String, Boolean> loggedUsers;
    private final transient Map<SocketChannel, String> userChannels;

    public UserRecords(Map<String, UserData> userDataMap,
                       Map<String, Boolean> loggedUsers,
                       Map<SocketChannel, String> userChannels) {
        validateUserDataMap(userDataMap);
        validateLoggedUsers(loggedUsers);
        validateUserChannels(userChannels);

        this.userDataMap = userDataMap;
        this.loggedUsers = loggedUsers;
        this.userChannels = userChannels;
    }

    public Map<String, UserData> userDataMap() {
        return userDataMap;
    }

    public Map<String, Boolean> loggedUsers() {
        return loggedUsers;
    }

    public Map<SocketChannel, String> userChannels() {
        return userChannels;
    }

    public void addUser(String username, UserData userData) {
        validateUsername(username);
        validateUserData(userData);

        userDataMap.put(username, userData);
        loggedUsers.put(username, false);
    }

    public void loginUser(String username, SocketChannel clientChannel) {
        validateUsername(username);
        validateSocketChannel(clientChannel);

        loggedUsers.put(username, true);
        userChannels.put(clientChannel, username);
    }

    public void logoutUser(String username, SocketChannel clientChannel) {
        validateUsername(username);
        validateSocketChannel(clientChannel);

        if (!loggedUsers.containsKey(username) || !userChannels.containsKey(clientChannel)) {
            throw new NoSuchUserException("There is no such user in the database to logout!");
        }

        loggedUsers.put(username, false);
        userChannels.remove(clientChannel, username);
    }

    public String getUserBySocketChannel(SocketChannel clientChannel) {
        validateSocketChannel(clientChannel);

        if (!userChannels.containsKey(clientChannel)) {
            throw new NoSuchUserException("There is no such user!");
        }

        return userChannels.get(clientChannel);
    }

    public void logoutAllUsers() {
        userDataMap.clear();
        loggedUsers.clear();
        userChannels.clear();
    }
}