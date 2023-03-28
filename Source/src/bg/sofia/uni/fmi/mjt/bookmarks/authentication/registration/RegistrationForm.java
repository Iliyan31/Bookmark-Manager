package bg.sofia.uni.fmi.mjt.bookmarks.authentication.registration;

@FunctionalInterface
public interface RegistrationForm {
    /**
     * In the constructor the user should only pass the username and their password
     * The only method in this class is register user which creates a record in the database for the user
     */
    void registerUser();
}