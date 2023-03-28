package bg.sofia.uni.fmi.mjt.bookmarks.authentication.registration;

import bg.sofia.uni.fmi.mjt.bookmarks.authentication.Validator;
import bg.sofia.uni.fmi.mjt.bookmarks.authentication.vault.SecurePassword;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.user.UserData;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Registration extends Validator implements RegistrationForm {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final String username;
    private final SecurePassword securePassword;
    private final UserDatabase userDatabase;
    private final String dateOfRegistration;

    public Registration(String username, String password, UserDatabase userDatabase) {
        validateUsername(username);
        validateExistingUser(username, userDatabase);
        validatePassword(password);
        validatePasswordForLength(password);
        validatePasswordNumber(password);
        validatePasswordCapitalLetter(password);
        validatePasswordSmallLetter(password);
        validatePasswordForSpecialCharacters(password);


        this.username = username;
        this.securePassword = new SecurePassword(password);
        this.dateOfRegistration = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        this.userDatabase = userDatabase;
    }

    public void registerUser() {
        UserData userData =
            new UserData(username, securePassword.getSecurePassword(), securePassword.getPasswordSalt(),
                dateOfRegistration);

        userDatabase.insertUserRecord(username, userData);
    }
}