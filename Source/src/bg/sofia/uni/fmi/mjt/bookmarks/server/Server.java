package bg.sofia.uni.fmi.mjt.bookmarks.server;

import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.bookmarks.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.bookmarks.BookmarkDatabase;
import bg.sofia.uni.fmi.mjt.bookmarks.repository.users.UserDatabase;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server extends Thread {
    private static final int BUFFER_SIZE = 8192;
    private static final String HOST = "localhost";
    private final CommandExecutor commandExecutor;
    private final UserDatabase userDatabase;
    private final BookmarkDatabase bookmarkDatabase;
    private final int port;
    private boolean isServerWorking;
    private ByteBuffer buffer;
    private Selector selector;
    private SelectionKey externalKey;

    public Server(int port, CommandExecutor commandExecutor, UserDatabase userDatabase,
                  BookmarkDatabase bookmarkDatabase) {
        this.port = port;
        this.commandExecutor = commandExecutor;
        this.userDatabase = userDatabase;
        this.bookmarkDatabase = bookmarkDatabase;
    }

    @Override
    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        externalKey = key;
                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientInput = getClientInput(clientChannel);
                            System.out.println(clientInput);
                            if (clientInput == null) {
                                continue;
                            }

                            String output =
                                commandExecutor.execute(CommandCreator.newCommand(clientInput), clientChannel);
                            writeClientOutput(clientChannel, output);

                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }

                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    for (SelectionKey key : selector.selectedKeys()) {
                        if (key.channel().equals(externalKey.channel())) {
                            key.cancel();
                        }
                    }

                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    public void shutdown() {
        this.isServerWorking = false;
        try {
            this.userDatabase.insertAllUsersToDatabase();
        } catch (IOException e) {
            System.out.println("The system was unable to insert all users data to the users database!");
        }
        try {
            this.bookmarkDatabase.insertBookmarkDataToDatabase();
        } catch (IOException e) {
            System.out.println("The system was unable to insert all users bookmarks to the bookmarks database!");
        }
        this.userDatabase.logoutAllUsers();
        this.bookmarkDatabase.clearAllData();
        this.buffer.clear();

        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}