package bg.sofia.uni.fmi.mjt.bookmarks.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    private static final String QUIT = "quit";
    private static final String CHARSET_NAME = "UTF-8";
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 4096;

    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();

                if (QUIT.equals(message)) {
                    buffer.clear();
                    break;
                }

                System.out.println("Sending message <" + message + "> to the server...");

                buffer.clear();
                buffer.put(message.getBytes());
                buffer.flip();
                socketChannel.write(buffer);

                buffer.clear();
                socketChannel.read(buffer);
                buffer.flip();

                byte[] byteArray = new byte[buffer.remaining()];
                buffer.get(byteArray);
                String reply = new String(byteArray, CHARSET_NAME);

                System.out.println("The server replied <" + reply + ">");
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}