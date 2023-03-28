package bg.sofia.uni.fmi.mjt.bookmarks.command;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.BookmarkBar;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Children;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.ChromeBookmarks;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.MetaInfo;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Other;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Roots;
import bg.sofia.uni.fmi.mjt.bookmarks.entities.chrome.Synced;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BitlyCommunicator;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManager;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabaseManager;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.logs.ErrorLogs;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CommandExecutorTest {
    static CommandExecutor commandExecutor;
    static SocketChannel channel;
    static BookmarkManager bookmarkManager;
    String returnedJson1 =
        "{\"created_at\":\"2023-02-07T10:00:22+0000\",\"id\":\"bit.ly/40s14jA\",\"link\":\"https://bit.ly/40s14jA\",\"custom_bitlinks\":[],\"long_url\":\"https://www.microsoft.com/bg-bg/windows\",\"archived\":false,\"tags\":[],\"deeplinks\":[],\"references\":{\"group\":\"https://api-ssl.bitly.com/v4/groups/Bn277pCF89T\"}}";
    static ChromeBookmarks chromeBookmarks;
    @Mock
    private static HttpClient httpClientMock;
    @Mock
    private static HttpResponse<String> httpBitlyResponseMock;

    @BeforeAll
    static void setUp() throws IOException, InterruptedException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");

        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");

        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/logs/logs.txt");


        httpClientMock = Mockito.mock(HttpClient.class);
        httpBitlyResponseMock = Mockito.mock(HttpResponse.class);

        when(httpClientMock.send(Mockito.any(HttpRequest.class),
            ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(httpBitlyResponseMock);
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");

        bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClientMock, bitlyCommunicator);

        channel = SocketChannel.open();
        MetaInfo metaInfo = new MetaInfo("");
        Children children = new Children("", "", "", "", metaInfo, "", "", "");
        List<Children> childrenList = new ArrayList<>();
        childrenList.add(children);
        chromeBookmarks =
            new ChromeBookmarks("a",
                new Roots(new BookmarkBar(childrenList, "", "", "", "", "", "", ""),
                    new Other(new ArrayList<>(), "", "", "", "", "", "", ""),
                    new Synced(new ArrayList<>(), "", "", "", "", "", "", "")),
                "a");

        commandExecutor = new CommandExecutor(bookmarkManager);
    }

    @BeforeEach
    void loginUser() {
        commandExecutor.execute(CommandCreator.newCommand("register Iliyan1 Hi1234567!"), channel);
        commandExecutor.execute(CommandCreator.newCommand("login Iliyan1 Hi1234567!"), channel);
        commandExecutor.execute(CommandCreator.newCommand("new-group group"), channel);
        //commandExecutor.execute(CommandCreator.newCommand("import-from-chrome"), channel);
    }

    @Test
    void testRegisterWithCorrectUsernameAndPassword() {
        assertEquals("Hi Iliyan3",
            commandExecutor.execute(CommandCreator.newCommand("register Iliyan3 Hi1234567!"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testRegisterWithPasswordBelowEightLength() {
        assertEquals("The password should be minimum 8 characters!",
            commandExecutor.execute(CommandCreator.newCommand("register Iliyan2 Hi127!"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testRegisterWithPasswordWithoutNumber() {
        assertEquals("The password should contain at least one number!",
            commandExecutor.execute(CommandCreator.newCommand("register Iliyan2 Hiiiiiiiiii!"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testRegisterWithPasswordWithoutCapitalLetter() {
        assertEquals("The password should contain at least one capital letter!",
            commandExecutor.execute(CommandCreator.newCommand("register Iliyan2 a1234567!"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testRegisterWithPasswordWithoutSmallLetter() {
        assertEquals("The password should contain at least one small letter!",
            commandExecutor.execute(CommandCreator.newCommand("register Iliyan2 A1234567!"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testRegisterWithPasswordWithoutSpecialCharacter() {
        assertEquals("The password should contain at least one special letter!",
            commandExecutor.execute(CommandCreator.newCommand("register Iliyan2 Hi1234567"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testRegisterWithIncorrectArgumentsLength() {
        assertEquals("The register user arguments cannot be different from two!",
            commandExecutor.execute(CommandCreator.newCommand("register"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testRegisterWithIncorrectNullValue() {
        Command command = new Command("register", null, "a");
        assertEquals("The none of the register arguments can be null, empty or blank!",
            commandExecutor.execute(command, channel), "The system should correctly register new user!");
    }

    @Test
    void testRegisterWithIncorrectNullValueCommand() {
        Command command = new Command("register", null);
        assertEquals("The register arguments cannot be null!", commandExecutor.execute(command, channel),
            "The system should correctly register new user!");
    }


    @Test
    void testLoginWithCorrectUsernameAndPassword() {
        String login = commandExecutor.execute(CommandCreator.newCommand("login Iliyan1 Hi1234567!"), channel);
        assertEquals("The user has successfully been logged!", login,
            "The system should correctly login new user!");
    }


    @Test
    void testLogout() {
        String logout = commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user has successfully been logged out!", logout,
            "The system should correctly login new user!");
    }

    @Test
    void testLoginWithPasswordBelowEightLength() {
        assertEquals("The password passed is incorrect!",
            commandExecutor.execute(CommandCreator.newCommand("login Iliyan1 Hi127!"), channel),
            "The system should correctly login new user!");
    }

    @Test
    void testLoginWithPasswordWithoutNumber() {
        assertEquals("The password passed is incorrect!",
            commandExecutor.execute(CommandCreator.newCommand("login Iliyan1 Hiiiiiiiiii!"), channel),
            "The system should correctly login new user!");
    }

    @Test
    void testLoginWithPasswordWithoutCapitalLetter() {
        assertEquals("The password passed is incorrect!",
            commandExecutor.execute(CommandCreator.newCommand("login Iliyan1 a1234567!"), channel),
            "The system should correctly login new user!");
    }

    @Test
    void testLoginWithPasswordWithoutSmallLetter() {
        assertEquals("The password passed is incorrect!",
            commandExecutor.execute(CommandCreator.newCommand("login Iliyan1 A1234567!"), channel),
            "The system should correctly login new user!");
    }

    @Test
    void testLoginWithPasswordWithoutSpecialCharacter() {
        assertEquals("The password passed is incorrect!",
            commandExecutor.execute(CommandCreator.newCommand("login Iliyan1 Hi1234567"), channel),
            "The system should correctly login new user!");
    }

    @Test
    void testLoginWithNoSuchUser() {
        assertEquals("There is no such user in the database!",
            commandExecutor.execute(CommandCreator.newCommand("login Iliyan2 Hi1234567"), channel),
            "The system should correctly login new user!");
    }


    @Test
    void testLoginWithIncorrectArgumentsLength() {
        assertEquals("The login user arguments cannot be different from two!",
            commandExecutor.execute(CommandCreator.newCommand("login"), channel),
            "The system should correctly login new user!");
    }

    @Test
    void testLoginWithIncorrectNullValue() {
        Command command = new Command("login", null, "a");
        assertEquals("The none of the login arguments can be null, empty or blank!",
            commandExecutor.execute(command, channel), "The system should correctly login new user!");
    }

    @Test
    void testLoginWithIncorrectNullValueCommand() {
        Command command = new Command("login", null);
        assertEquals("The login arguments cannot be null!", commandExecutor.execute(command, channel),
            "The system should correctly login new user!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedLogout() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("logout"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedNewGroup() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("new-group group"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedAddTo() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("add-to group asdf"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedRemoveFrom() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("remove-from group asdf"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedList() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("list"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedSearch() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("search --title asdf"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedCleanup() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("cleanup"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedImportFromChrome() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("import-from-chrome"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testAccessSystemWithoutBeingLoggedGetChromeBookmarks() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        assertEquals("The user should be logged in order to access the system!",
            commandExecutor.execute(CommandCreator.newCommand("get-chrome-bookmarks"), channel),
            "The system should correctly protect the systems from users who have not been logged!");
    }

    @Test
    void testUnknownCommand() {
        assertEquals("Unknown command",
            commandExecutor.execute(CommandCreator.newCommand("get"), channel),
            "The system should correctly handle unknown command");
    }


    @Test
    void testLogoutWithIncorrectArgumentsLength() {
        assertEquals("The logout user arguments cannot be different from zero!",
            commandExecutor.execute(CommandCreator.newCommand("logout as"), channel),
            "The system should correctly login new user!");
    }

    @Test
    void testLogoutWithIncorrectNullValue() {
        Command command = new Command("logout", null, "a");
        assertEquals("The none of the logout arguments can be null, empty or blank!",
            commandExecutor.execute(command, channel), "The system should correctly login new user!");
    }

    @Test
    void testLogoutWithIncorrectNullValueCommand() {
        Command command = new Command("logout", null);
        assertEquals("The logout arguments cannot be null!", commandExecutor.execute(command, channel),
            "The system should correctly login new user!");
    }

    @Test
    void testCreateNewGroup() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
        commandExecutor.execute(CommandCreator.newCommand("register Iliyan4 Hi1234567!"), channel);
        commandExecutor.execute(CommandCreator.newCommand("login Iliyan4 Hi1234567!"), channel);
        assertEquals("The group was successfully added",
            commandExecutor.execute(CommandCreator.newCommand("new-group group"), channel),
            "The system should correctly add new group");
    }

    @Test
    void testAddToWithExistingGroup() {
        assertEquals("Successfully added bookmark without shortened url!",
            commandExecutor.execute(
                CommandCreator.newCommand("add-to group https://en.wikipedia.org/wiki/Java_(programming_language)"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testAddToWithNonExistingGroup() {
        assertEquals("There is no such group!",
            commandExecutor.execute(
                CommandCreator.newCommand("add-to group1 https://en.wikipedia.org/wiki/Java_(programming_language)"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testRemoveFrom() {
        commandExecutor.execute(
            CommandCreator.newCommand("add-to group https://en.wikipedia.org/wiki/Java_(programming_language)"),
            channel);

        assertEquals("Successfully removed bookmark!", commandExecutor.execute(CommandCreator.newCommand(
                    "remove-from group https://en.wikipedia.org/wiki/Java_(programming_language)"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testList() {
        commandExecutor.execute(
            CommandCreator.newCommand("add-to group https://en.wikipedia.org/wiki/Java_(programming_language)"),
            channel);
        assertEquals(
            "[{\"title\":\"Java (programming language) - Wikipedia\",\"url\":\"https://en.wikipedia.org/wiki/Java_(programming_language)\",\"short_url\":\"\",\"tags\":[\"java\",\"original\",\"retriev\",\"archiv\",\"oracle\",\"sun\",\"class\",\"programm\",\"language\",\"2014\",\"platform\",\"se\",\"september\",\"main\",\"object\",\"languages\",\"code\",\"january\",\"machine\",\"march\",\"android\",\"2019\",\"implementation\",\"system\",\"classes\",\"2020\",\"public\",\"december\",\"memory\",\"10\"]}]",
            commandExecutor.execute(CommandCreator.newCommand("list"), channel),
            "The system should correctly add new group");
    }

    @Test
    void testListFromGroup() {
        commandExecutor.execute(
            CommandCreator.newCommand("add-to group https://en.wikipedia.org/wiki/Java_(programming_language)"),
            channel);
        assertEquals(
            "[{\"title\":\"Java (programming language) - Wikipedia\",\"url\":\"https://en.wikipedia.org/wiki/Java_(programming_language)\",\"short_url\":\"\",\"tags\":[\"java\",\"original\",\"retriev\",\"archiv\",\"oracle\",\"sun\",\"class\",\"programm\",\"language\",\"2014\",\"platform\",\"se\",\"september\",\"main\",\"object\",\"languages\",\"code\",\"january\",\"machine\",\"march\",\"android\",\"2019\",\"implementation\",\"system\",\"classes\",\"2020\",\"public\",\"december\",\"memory\",\"10\"]}]",
            commandExecutor.execute(
                CommandCreator.newCommand("list --group-name group"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testSearchByTitle() {
        commandExecutor.execute(
            CommandCreator.newCommand("add-to group https://en.wikipedia.org/wiki/Java_(programming_language)"),
            channel);
        assertEquals(
            "[{\"title\":\"Java (programming language) - Wikipedia\",\"url\":\"https://en.wikipedia.org/wiki/Java_(programming_language)\",\"short_url\":\"\",\"tags\":[\"java\",\"original\",\"retriev\",\"archiv\",\"oracle\",\"sun\",\"class\",\"programm\",\"language\",\"2014\",\"platform\",\"se\",\"september\",\"main\",\"object\",\"languages\",\"code\",\"january\",\"machine\",\"march\",\"android\",\"2019\",\"implementation\",\"system\",\"classes\",\"2020\",\"public\",\"december\",\"memory\",\"10\"]}]",
            commandExecutor.execute(
                CommandCreator.newCommand("search --title language"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testSearchByTitleMultipleWords() {
        commandExecutor.execute(
            CommandCreator.newCommand("add-to group https://en.wikipedia.org/wiki/Java_(programming_language)"),
            channel);
        assertEquals(
            "[{\"title\":\"Java (programming language) - Wikipedia\",\"url\":\"https://en.wikipedia.org/wiki/Java_(programming_language)\",\"short_url\":\"\",\"tags\":[\"java\",\"original\",\"retriev\",\"archiv\",\"oracle\",\"sun\",\"class\",\"programm\",\"language\",\"2014\",\"platform\",\"se\",\"september\",\"main\",\"object\",\"languages\",\"code\",\"january\",\"machine\",\"march\",\"android\",\"2019\",\"implementation\",\"system\",\"classes\",\"2020\",\"public\",\"december\",\"memory\",\"10\"]}]",
            commandExecutor.execute(
                CommandCreator.newCommand("search --title programming language"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testSearchByTags() {
        commandExecutor.execute(
            CommandCreator.newCommand("add-to group https://en.wikipedia.org/wiki/Java_(programming_language)"),
            channel);
        assertEquals(
            "[{\"title\":\"Java (programming language) - Wikipedia\",\"url\":\"https://en.wikipedia.org/wiki/Java_(programming_language)\",\"short_url\":\"\",\"tags\":[\"java\",\"original\",\"retriev\",\"archiv\",\"oracle\",\"sun\",\"class\",\"programm\",\"language\",\"2014\",\"platform\",\"se\",\"september\",\"main\",\"object\",\"languages\",\"code\",\"january\",\"machine\",\"march\",\"android\",\"2019\",\"implementation\",\"system\",\"classes\",\"2020\",\"public\",\"december\",\"memory\",\"10\"]}]",
            commandExecutor.execute(
                CommandCreator.newCommand("search --tags implementation system"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testImportFromChrome() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");

        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");

        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/logs/logs.txt");

        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");

        BookmarkManager bookmarkManager1 = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClientMock, bitlyCommunicator);

        CommandExecutor commandExecutor1 = new CommandExecutor(bookmarkManager1);

        commandExecutor1.execute(CommandCreator.newCommand("register Iliyan1 Hi1234567!"), channel);
        commandExecutor1.execute(CommandCreator.newCommand("login Iliyan1 Hi1234567!"), channel);
        commandExecutor1.execute(CommandCreator.newCommand("new-group group"), channel);

        assertEquals("Successfully added bookmarks from Chrome browser!", commandExecutor1.execute(
                CommandCreator.newCommand("import-from-chrome"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testIOExceptionRegister() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("register Iliyan2 Hi1234567"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionLogin() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("login Iliyan2 Hi1234567"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionNewGroup() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("new-group group"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionAddTo() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("add-to group2 asdf"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionRemoveFrom() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("remove-from group2 asdf"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionSearch() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("search --tags a"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionList() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("list --group-name group2"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionCleanUp() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("cleanup"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionImportFromChrome() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("import-from-chrome"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionLogout() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("logout"), channel),
            "The system should correctly register new user!");
    }

    @Test
    void testIOExceptionGetChromeBookmarks() throws IOException {
        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");
        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");
        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/mjt/bookmarks/repository/logs/logs.txt");
        HttpClient httpClient = HttpClient.newBuilder().build();
        BitlyCommunicator bitlyCommunicator = new BitlyCommunicator(httpClientMock, "YourApiKey");
        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt", errorLogs, httpClient, bitlyCommunicator);
        SocketChannel channel = SocketChannel.open();
        CommandExecutor commandExecutor = new CommandExecutor(bookmarkManager);

        assertEquals("src\\bg\\sofia\\uni\\mjt\\bookmarks\\repository\\logs\\logs.txt",
            commandExecutor.execute(CommandCreator.newCommand("get-chrome-bookmarks"), channel),
            "The system should correctly register new user!");
    }


    @Test
    void testAddToGroupWithShorten() {
        when(httpBitlyResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpBitlyResponseMock.body()).thenReturn(returnedJson1);

        assertEquals("Successfully added bookmark with shortened url!", commandExecutor.execute(
                CommandCreator.newCommand(
                    "add-to group https://en.wikipedia.org/wiki/Java_(programming_language) --shorten"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testCleanUp() {
        commandExecutor.execute(
            CommandCreator.newCommand("add-to group https://github.com/fmi/java-course/tree/master/11-network-ii"),
            channel);

        when(httpBitlyResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

        assertEquals("Successfully removed invalid bookmarks!", commandExecutor.execute(
                CommandCreator.newCommand("cleanup"),
                channel),
            "The system should correctly add new group");
    }

    @Test
    void testGetAllChromeBookmarks() {
        bookmarkManager = Mockito.spy(bookmarkManager);
        Mockito.doReturn(chromeBookmarks).when(bookmarkManager).getChromeBookmarks(Mockito.any());

        assertEquals("{\"checksum\":\"\",\"version\":\"\"}", commandExecutor.execute(
                CommandCreator.newCommand("get-chrome-bookmarks"),
                channel),
            "The system should correctly add new group");
    }

    @AfterEach
    void logoutUser() {
        commandExecutor.execute(CommandCreator.newCommand("logout"), channel);
    }

    @AfterAll
    static void cleanUp() throws IOException {
        channel.close();
    }
}