package bg.sofia.uni.fmi.mjt.bookmarks.authentication.vault;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SecurePasswordTest {
    static SecurePassword securePassword;

    @BeforeAll
    static void setUp() {
        securePassword = new SecurePassword("Hi1234567!");
    }

    @Test
    void testCreateSecurePasswordWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> new SecurePassword(null),
            "The password cannot be null, empty or blank!");
    }

    @Test
    void testCreateSecurePasswordWithEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> new SecurePassword(""),
            "The password cannot be null, empty or blank!");
    }

    @Test
    void testCreateSecurePasswordWithBlankPassword() {
        assertThrows(IllegalArgumentException.class, () -> new SecurePassword(" "),
            "The password cannot be null, empty or blank!");
    }

    @Test
    void testReconstructPasswordNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> securePassword.reconstructPassword(null, null),
            "The password cannot be null, empty or blank!");
    }

    @Test
    void testReconstructPasswordNullSalt() {
        assertThrows(IllegalArgumentException.class, () -> securePassword.reconstructPassword("Hi1234567!", null),
            "The salt cannot be null!");
    }

    @Test
    void testReconstructPassword() {
        assertEquals("b22342544d220e971cb1cb373afd7c3c7ad3fa8e7a658ab8ebf4815bad8082",
            securePassword.reconstructPassword("Hi1234567!", new byte[] {-1, -2, -3}),
            "The salt cannot be null!");
    }

    @Test
    void testGetSaltNoSalt() {
        assertThrows(NoSuchElementException.class, securePassword::getPasswordSalt,
            "The salt cannot be accessed when cannot be null!");
    }

    @Test
    void testGetSalt() {
        securePassword.getSecurePassword();
        byte[] bytes = securePassword.getPasswordSalt();
        assertEquals(bytes, securePassword.getPasswordSalt(), "The salt cannot be null!");
    }
}