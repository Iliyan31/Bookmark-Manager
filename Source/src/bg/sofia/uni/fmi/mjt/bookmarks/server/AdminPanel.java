package bg.sofia.uni.fmi.mjt.bookmarks.server;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BitlyCommunicator;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.BookmarkManager;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabaseManager;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.logs.ErrorLogs;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabaseManager;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Scanner;

public class AdminPanel {
    public static void main(String[] args) throws IOException, InterruptedException {
        final int serverPortNumber = 7777;

        UserDatabase userDatabase =
            new UserDatabaseManager("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/users/UserDatabase.json");

        BookmarkDatabase bookmarkDatabase = new BookmarkDatabaseManager(
            "src/bg/sofia/uni/fmi/mjt/bookmarks/repository/bookmarks/BookmarksDatabase.json");

        ErrorLogs errorLogs = new ErrorLogs("src/bg/sofia/uni/fmi/mjt/bookmarks/repository/logs/logs.txt");

        HttpClient httpClient = HttpClient.newBuilder().build();

        BitlyCommunicator bitlyCommunicator =
            new BitlyCommunicator(httpClient, "YourApiKey");

        BookmarkManager bookmarkManager = new BookmarkManager(userDatabase, bookmarkDatabase,
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/stopwordsEN.txt",
            "src/bg/sofia/uni/fmi/mjt/bookmarks/manager/files/suffixes.txt",
            errorLogs, httpClient, bitlyCommunicator);


        Server server = new Server(serverPortNumber, new CommandExecutor(bookmarkManager), userDatabase,
            bookmarkDatabase);


        Scanner adminInput = new Scanner(System.in);
        String adminCommand;

        System.out.print("Please input command: ");
        while (true) {
            adminCommand = adminInput.nextLine();

            if (adminCommand.equals("start")) {
                System.out.println("Starting server");
                System.out.println("Listening for client requests");
                server.start();
            }
            if (adminCommand.equals("stop")) {
                if (server.isAlive()) {
                    System.out.println("Stopping server...");
                    server.shutdown();
                }
                break;
            }
        }

        adminInput.close();
        server.join();
    }
}