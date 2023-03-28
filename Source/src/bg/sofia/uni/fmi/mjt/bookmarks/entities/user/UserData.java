package bg.sofia.uni.fmi.mjt.bookmarks.entities.user;

public record UserData(String username, String password, byte[] salt, String registerDate) {
    public UserData {
        if (username == null || username.isEmpty() || username.isBlank()) {
            throw new IllegalArgumentException("Error occurred while passing the username!");
        }

        if (password == null || password.isEmpty() || password.isBlank()) {
            throw new IllegalArgumentException("Error occurred while passing the encrypted password for the user!");
        }

        if (salt == null) {
            throw new IllegalArgumentException("Error occurred while passing the encryption data for the user!");
        }

        if (registerDate == null || registerDate.isEmpty() || registerDate.isBlank()) {
            throw new IllegalArgumentException("Error occurred while passing the registerDate for the user!");
        }
    }
}