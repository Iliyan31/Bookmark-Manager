package bg.sofia.uni.fmi.mjt.bookmarks.authentication.vault;

import bg.sofia.uni.fmi.mjt.bookmarks.authentication.Validator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.Random;

public class SecurePassword extends Validator {
    private static final int HEX_255 = 0xff;
    private static final int SALT_LENGTH = 20;
    private final String password;
    private byte[] passwordSalt;

    public SecurePassword(String password) {
        validatePassword(password);

        this.password = password;
    }

    public String reconstructPassword(String password, byte[] passwordSalt) {
        validatePassword(password);
        validateSalt(passwordSalt);

        try {
            return hashPassword(password, passwordSalt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("The system was unable to reconstruct your password! Please try again!");
        }
    }

    public String getSecurePassword() {
        try {
            return hashPassword(password, getRandomSaltedByteArray());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                "The system was unable to secure your password due to unexpected error occurred! Please try again!");
        }
    }

    public byte[] getPasswordSalt() {
        if (passwordSalt == null) {
            throw new NoSuchElementException("The user has not have generated secure password to have any salt!");
        }

        return passwordSalt;
    }

    private String hashPassword(String password, byte[] passwordSalt) throws NoSuchAlgorithmException {
        MessageDigest digest;

        digest = MessageDigest.getInstance("SHA-256");
        digest.update(passwordSalt);

        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return getHashedPasswordFromByteArray(encodedHash);
    }

    private String getHashedPasswordFromByteArray(byte[] encodedHash) {
        StringBuilder hashedPassword = new StringBuilder();

        for (byte b : encodedHash) {
            hashedPassword.append(Integer.toHexString(HEX_255 & b));
        }

        return hashedPassword.toString();
    }

    private byte[] getRandomSaltedByteArray() {
        Random random = new SecureRandom();

        passwordSalt = new byte[SALT_LENGTH];
        random.nextBytes(passwordSalt);
        return passwordSalt;
    }
}