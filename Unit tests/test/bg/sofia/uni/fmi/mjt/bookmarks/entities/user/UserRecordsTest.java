package bg.sofia.uni.fmi.mjt.bookmarks.entities.user;

import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.system.database.NoSuchUserException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRecordsTest {
    static UserData userData;
    static UserRecords userRecords;
    static SocketChannel channel;

    @BeforeAll
    static void setUp() throws IOException {
        userData = new UserData("Iliyan", "Hi1234567!", new byte[] {1}, LocalDateTime.now().toString());
        userRecords = new UserRecords(new HashMap<>(), new HashMap<>(), new HashMap<>());
        channel = SocketChannel.open();
    }

    @Test
    void testCreateUserRecordsWithNullUserDataMap() {
        assertThrows(IllegalArgumentException.class, () -> new UserRecords(null, new HashMap<>(), new HashMap<>()),
            "The system should correctly handle null creation of user records!");
    }

    @Test
    void testCreateUserRecordsWithNullLoggedUsers() {
        assertThrows(IllegalArgumentException.class, () -> new UserRecords(new HashMap<>(), null, new HashMap<>()),
            "The system should correctly handle null creation of user records!");
    }

    @Test
    void testCreateUserRecordsWithNullUserChannels() {
        assertThrows(IllegalArgumentException.class, () -> new UserRecords(new HashMap<>(), new HashMap<>(), null),
            "The system should correctly handle null creation of user records!");
    }

    @Test
    void testAddUserNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.addUser(null, userData),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testAddUserEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.addUser("", userData),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testAddUserBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.addUser(" ", userData),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testAddUserNullUserData() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.addUser("a", null),
            "The system should correctly handle null user data!");
    }

    @Test
    void testAddUserLoggedUsers() {
        userRecords.addUser("Iliyan", userData);
        assertFalse(userRecords.loggedUsers().get("Iliyan"), "The system should correctly add user!");
    }

    @Test
    void testAddUserUserDataMap() {
        userRecords.addUser("Iliyan", userData);
        assertEquals(userRecords.userDataMap().get("Iliyan").username(), "Iliyan",
            "The system should correctly add user!");
    }

    @Test
    void testLoginUserNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.loginUser(null, channel),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testLoginUserEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.loginUser("", channel),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testLoginUserBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.loginUser(" ", channel),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testLoginUserNullSocketChannel() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.loginUser("a", null),
            "The system should correctly handle null user channel!");
    }

    @Test
    void testLoginUserLoggedUsers() {
        userRecords.loginUser("Iliyan", channel);
        assertTrue(userRecords.loggedUsers().get("Iliyan"), "The system should correctly login user!");
    }

    @Test
    void testLoginUserUserChannels() {
        userRecords.loginUser("Iliyan", channel);
        assertEquals(userRecords.userChannels().get(channel), "Iliyan",
            "The system should correctly login user!");
    }

    @Test
    void testLogoutUserNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.logoutUser(null, channel),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testLogoutUserEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.logoutUser("", channel),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testLogoutUserBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.logoutUser(" ", channel),
            "The system should correctly handle null, empty or blank username!");
    }

    @Test
    void testLogoutUserNullSocketChannel() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.logoutUser("a", null),
            "The system should correctly handle null user channel!");
    }

    @Test
    void testLogoutUserLoggedUsers() {
        userRecords.loginUser("Iliyan", channel);
        userRecords.logoutUser("Iliyan", channel);
        assertFalse(userRecords.loggedUsers().get("Iliyan"), "The system should correctly logout user!");
    }

    @Test
    void testLogoutUserUserChannels() {
        userRecords.loginUser("Iliyan", channel);
        userRecords.logoutUser("Iliyan", channel);
        assertNull(userRecords.userChannels().get(channel), "The system should correctly logout user!");
    }


    @Test
    void testLogoutUserWithNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userRecords.logoutUser("Iliyan1", channel),
            "The system should correctly add user!");
    }

    @Test
    void testLogoutUserWithNoChannel() {
        userRecords.loginUser("Iliyan", channel);
        assertThrows(NoSuchUserException.class, () -> userRecords.logoutUser("Iliyan", SocketChannel.open()),
            "The system should correctly add user!");
    }

    @Test
    void testGetUserBySocketChannelNullChannel() {
        assertThrows(IllegalArgumentException.class, () -> userRecords.getUserBySocketChannel(null),
            "The system should correctly handle null user channel!");
    }

    @Test
    void testGetUserBySocketChannelNoSuchUser() {
        assertThrows(NoSuchUserException.class, () -> userRecords.getUserBySocketChannel(SocketChannel.open()),
            "The system should correctly handle no such user!");
    }

    @Test
    void testGetUserBySocketChannel() {
        userRecords.loginUser("Iliyan", channel);
        assertEquals("Iliyan", userRecords.getUserBySocketChannel(channel),
            "The system should correctly return user by socket channel!");
    }

    @Test
    void testLogoutAllUsersUserDataMap() {
        userRecords.logoutAllUsers();
        assertEquals(0, userRecords.userDataMap().size(), "The user data should be cleared after logout all users!");
    }

    @Test
    void testLogoutAllUsersLoggedUsers() {
        userRecords.logoutAllUsers();
        assertEquals(0, userRecords.loggedUsers().size(),
            "The logged users data should be cleared after logout all users!");
    }

    @Test
    void testLogoutAllUsersUserChannels() {
        userRecords.logoutAllUsers();
        assertEquals(0, userRecords.userChannels().size(),
            "The user channels data should be cleared after logout all users!");
    }
}