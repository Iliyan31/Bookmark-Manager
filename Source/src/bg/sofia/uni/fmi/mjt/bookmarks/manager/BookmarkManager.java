package bg.sofia.uni.fmi.mjt.bookmarks.manager;

import bg.sofia.uni.fmi.mjt.bookmarks.authentication.login.Login;
import bg.sofia.uni.fmi.mjt.bookmarks.authentication.registration.Registration;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.bookmark.Bookmark;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.logs.ErrorLogs;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookmarkManager extends BookmarkManagerValidator implements BookmarkManagerAPI {
    private static final String ANY_CHAR_EXCEPT_LETTERS_NUM = "[^0-9A-Za-z']";
    private static final int MINIMUM_WORD_LENGTH = 2;
    private static final int MOST_USED_KEYWORDS_LEN = 30;
    private static final int BROKEN_URL_CODE = 404;
    private final Collection<String> stopWords;
    private final Collection<String> suffixes;
    private final UserDatabase userDatabase;
    private final BookmarkDatabase bookmarkDatabase;
    private final ErrorLogs errorLogs;
    private final HttpClient httpClient;
    private final BitlyCommunicator bitlyCommunicator;

    public BookmarkManager(UserDatabase userDatabase, BookmarkDatabase bookmarkDatabase,
                           String stopWordsPath, String suffixesPath,
                           ErrorLogs errorLogs, HttpClient httpClient, BitlyCommunicator bitlyCommunicator) throws IOException {
        validateUserDatabase(userDatabase);
        validateBookmarkDatabase(bookmarkDatabase);
        validateStopWordsPath(stopWordsPath);
        validateSuffixesPath(suffixesPath);
        validateErrorLogs(errorLogs);
        validateHttpClient(httpClient);

        this.stopWords = new HashSet<>();
        this.suffixes = new HashSet<>();
        this.userDatabase = userDatabase;
        this.bookmarkDatabase = bookmarkDatabase;
        this.errorLogs = errorLogs;
        this.httpClient = httpClient;
        this.bitlyCommunicator = bitlyCommunicator;

        extractAllWords(Path.of(stopWordsPath), stopWords);
        extractAllWords(Path.of(suffixesPath), suffixes);
    }

    @Override
    public void register(String username, String password) {
        Registration registration = new Registration(username, password, userDatabase);
        registration.registerUser();
    }

    @Override
    public void login(String username, String password, SocketChannel clientChannel) {
        Login login = new Login(username, password, userDatabase, clientChannel);
        login.loginUser();
    }

    @Override
    public void logout(SocketChannel clientChannel) {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        userDatabase.logoutUser(username, clientChannel);
    }

    @Override
    public void createNewGroup(String groupName, SocketChannel clientChannel) {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        bookmarkDatabase.createNewGroupForUser(username, groupName);
    }

    @Override
    public void addBookmarkToGroup(String groupName, String bookmarkURL, SocketChannel clientChannel, boolean shorten)
        throws IOException, URISyntaxException, InterruptedException {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);

        if (!shorten) {
            Bookmark bookmark = createBookmark(bookmarkURL, "");
            bookmarkDatabase.addBookmarkToUserGroup(username, groupName, bookmark);
        } else {
            String shortenedURL = bitlyCommunicator.shortenBookmarkURL(bookmarkURL);
            Bookmark bookmark = createBookmark(bookmarkURL, shortenedURL);

            bookmarkDatabase.addBookmarkToUserGroup(username, groupName, bookmark);
        }
    }

    @Override
    public void removeBookmarkFromGroup(String groupName, String bookmarkURL, SocketChannel clientChannel) {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        bookmarkDatabase.removeBookmarkFromGroup(username, groupName, bookmarkURL);
    }

    @Override
    public Collection<Bookmark> listAllBookmarks(SocketChannel clientChannel) {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        return bookmarkDatabase.getAllUserBookmarks(username);
    }

    @Override
    public Collection<Bookmark> listAllBookmarks(String groupName, SocketChannel clientChannel) {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        return bookmarkDatabase.getAllUserBookmarksFromGroup(username, groupName);
    }

    @Override
    public Collection<Bookmark> searchBookmarksByTags(Collection<String> tags, SocketChannel clientChannel) {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        return bookmarkDatabase.getBookmarksByTags(username, tags);
    }

    @Override
    public Collection<Bookmark> searchBookmarksByTitle(String title, SocketChannel clientChannel) {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        return bookmarkDatabase.getBookmarksByTitle(username, title);
    }

    @Override
    public void cleanupBookmarks(SocketChannel clientChannel)
        throws URISyntaxException, InterruptedException, IOException {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);

        bookmarkDatabase.removeBookmarks(username,
            getAllBookmarksToClean(bookmarkDatabase.getAllUserBookmarks(username)));
    }

    @Override
    public void importFromChrome(SocketChannel clientChannel) throws IOException {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        OSManager osManager = new OSManager();
        ChromeBookmarks chromeBookmarks = osManager.getChromeBookmarks();
        bookmarkDatabase.addChromeBookmarks(username, chromeBookmarks);
    }

    @Override
    public ChromeBookmarks getChromeBookmarks(SocketChannel clientChannel) {
        validateLoggedUser(userDatabase, clientChannel);

        String username = getUsername(clientChannel);
        return bookmarkDatabase.getChromeBookmarks(username);
    }

    @Override
    public void addLogs(Exception e) throws IOException {
        errorLogs.appendLogs(e);
    }

    private String getUsername(SocketChannel clientChannel) {
        return userDatabase.getUserBySocketChannel(clientChannel);
    }

    private Bookmark createBookmark(String bookmarkURL, String shortenedURL) throws IOException {
        Document document = Jsoup.connect(bookmarkURL).get();
        String bookmarkTitle = document.title();

        return new Bookmark(bookmarkTitle, bookmarkURL, shortenedURL, getMostFrequentTags(getBookmarkTags(document)));
    }

    private Collection<String> getBookmarkTags(Document document) {
        String bodyText = document.body().text().toLowerCase();

        return Arrays.stream(bodyText.split(ANY_CHAR_EXCEPT_LETTERS_NUM))
            .filter(w -> !w.isEmpty() && !w.isBlank() && w.length() >= MINIMUM_WORD_LENGTH && !stopWords.contains(w))
            .map(this::suffixStrip)
            .collect(Collectors.toList());
    }

    private Collection<String> getMostFrequentTags(Collection<String> allBookmarkTags) {
        Map<String, Integer> allTagsCount =
            allBookmarkTags.stream()
                .collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));

        return allTagsCount.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
            .limit(BookmarkManager.MOST_USED_KEYWORDS_LEN)
            .map(Map.Entry::getKey)
            .toList();
    }

    private String suffixStrip(String word) {
        for (String suffix : suffixes) {
            if (word.endsWith(suffix)) {
                return word.substring(0, word.length() - suffix.length());
            }
        }

        return word;
    }

    private void extractAllWords(Path path, Collection<String> setToFill) throws IOException {
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.forEach(setToFill::add);
        }
    }

    private boolean checkBookmarkValidity(Bookmark bookmark)
        throws URISyntaxException, InterruptedException, IOException {
        URI uri = new URI(bookmark.bookmarkURL());

        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == BROKEN_URL_CODE;
    }

    private Collection<Bookmark> getAllBookmarksToClean(Collection<Bookmark> allBookmarks)
        throws URISyntaxException, InterruptedException, IOException {

        Set<Bookmark> bookmarkSet = new HashSet<>();

        for (Bookmark bookmark : allBookmarks) {
            if (checkBookmarkValidity(bookmark)) {
                bookmarkSet.add(bookmark);
            }
        }

        return bookmarkSet;
    }
}