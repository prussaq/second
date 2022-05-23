import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class NioServer implements Runnable {

    private final static int PORT = 9999;
    private int count;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer buffer;
    private final ByteBuffer welcomeBuffer = ByteBuffer.wrap("Welcome to the chat!\n".getBytes(StandardCharsets.UTF_8));

    public NioServer() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        buffer = ByteBuffer.allocate(256);
    }

    @Override
    public void run() {
        System.out.println("Server running on port " + PORT);

        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        String client = "Client #" + ++count;
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ, client);

        System.out.printf("New client %s connected.\n", client);

        clientChannel.write(welcomeBuffer);
        welcomeBuffer.rewind();
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        StringBuilder stringBuilder = new StringBuilder();

        int read;
        buffer.clear();

        while ((read = clientChannel.read(buffer)) > 0) {
            buffer.flip();
            stringBuilder.append(new String(Arrays.copyOf(buffer.array(), buffer.limit())));
            buffer.clear();
        }

        String message;

        if (read < 0) {
            message = key.attachment() + " left the chat\n";
            clientChannel.close();
        } else { // read == 0
            message = key.attachment() + ": " + stringBuilder;
        }

        System.out.println(message);
        broadcastMessage(message);
    }

    private void broadcastMessage(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));

        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel channel = (SocketChannel) key.channel();
                channel.write(buffer);
                buffer.rewind();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new NioServer()).start();
    }
}
