package bg.sofia.uni.fmi.mjt.bookmarks.authentication.login;

import bg.sofia.uni.fmi.mjt.bookmarks.authentication.Validator;
import bg.sofia.uni.fmi.mjt.bookmarks.authentication.vault.SecurePassword;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.user.UserData;
import bg.sofia.uni.fmi.mjt.bookmarks.exceptions.user.login.WrongUserPasswordException;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;

import java.nio.channels.SocketChannel;

public class Login extends Validator {
    private final String username;
    private final String password;
    private final UserDatabase userDatabase;
    private final SocketChannel socketChannel;

    public Login(String username, String password, UserDatabase userDatabase,
                 SocketChannel clientChannel) {
        validateUsername(username);
        validatePassword(password);
        validateUserDatabaseManager(userDatabase);
        validateSocketChannel(clientChannel);

        this.username = username;
        this.password = password;
        this.userDatabase = userDatabase;
        this.socketChannel = clientChannel;
    }

    public void loginUser() {
        UserData userData = userDatabase.getSpecificUserData(username);
        validateUserData(userData);

        String reconstructedPassword = new SecurePassword(password).reconstructPassword(password, userData.salt());

        if (userData.password().equals(reconstructedPassword)) {
            userDatabase.loginUser(username, socketChannel);
        } else {
            throw new WrongUserPasswordException("The password passed is incorrect!");
        }
    }
}