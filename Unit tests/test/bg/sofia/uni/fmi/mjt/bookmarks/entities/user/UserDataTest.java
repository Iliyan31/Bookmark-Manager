package bg.sofia.uni.fmi.mjt.bookmarks.entities.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDataTest {
    @Test
    void testCreateUserDataWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new UserData(null, "a", new byte[] {1}, "a"),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new UserData("", "a", new byte[] {1}, "a"),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new UserData(" ", "a", new byte[] {1}, "a"),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new UserData("null", null, new byte[] {1}, "a"),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> new UserData("a", "", new byte[] {1}, "a"),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithBlankPassword() {
        assertThrows(IllegalArgumentException.class, () -> new UserData("a", " ", new byte[] {1}, "a"),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithNullSalt() {
        assertThrows(IllegalArgumentException.class, () -> new UserData("a", "a", null, "a"),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithNullRegisterDate() {
        assertThrows(IllegalArgumentException.class, () -> new UserData("null", "null", new byte[] {1}, null),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithEmptyRegisterDate() {
        assertThrows(IllegalArgumentException.class, () -> new UserData("a", "a", new byte[] {1}, ""),
            "The system should correctly catch errors when creating user data!");
    }

    @Test
    void testCreateUserDataWithBlankRegisterDate() {
        assertThrows(IllegalArgumentException.class, () -> new UserData("a", "a", new byte[] {1}, " "),
            "The system should correctly catch errors when creating user data!");
    }
}
