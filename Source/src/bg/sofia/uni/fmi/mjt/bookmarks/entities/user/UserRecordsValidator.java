package bg.sofia.uni.fmi.mjt.bookmarks.entities.user;

import java.nio.channels.SocketChannel;
import java.util.Map;

public abstract class UserRecordsValidator {
    void validateUserDataMap(Map<String, UserData> userDataMap) {
        if (userDataMap == null) {
            throw new IllegalArgumentException("The are no registered users!");
        }
    }

    void validateLoggedUsers(Map<String, Boolean> loggedUsers) {
        if (loggedUsers == null) {
            throw new IllegalArgumentException("There are no users in the system!");
        }
    }

    void validateUserChannels(Map<SocketChannel, String> userChannels) {
        if (userChannels == null) {
            throw new IllegalArgumentException("There are no connected users!");
        }
    }

    void validateUsername(String username) {
        if (username == null || username.isEmpty() || username.isBlank()) {
            throw new IllegalArgumentException("The username passed cannot be null, empty or blank!");
        }
    }

    void validateSocketChannel(SocketChannel clientChannel) {
        if (clientChannel == null) {
            throw new IllegalArgumentException("The client channel passed cannot be null!");
        }
    }

    void validateUserData(UserData userData) {
        if (userData == null) {
            throw new IllegalArgumentException("The user data passed cannot be null!");
        }
    }
}
