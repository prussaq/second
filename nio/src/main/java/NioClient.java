import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class NioClient {

    private SocketChannel socketChannel;
    private ByteBuffer buffer;

    public void start() throws IOException, InterruptedException {
        socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 9999));
        socketChannel.configureBlocking(false);
        buffer = ByteBuffer.allocate(256);
        Thread.sleep(100);
        readChat();
    }

    public void stop() throws IOException {
        socketChannel.close();
        buffer = null;
    }

    public void sendMessage(String message) throws IOException, InterruptedException {
        buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer);
        buffer.clear();
        Thread.sleep(100);
        readChat();
    }

    private void readChat() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        buffer.clear();

        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            stringBuilder.append(new String(Arrays.copyOf(buffer.array(), buffer.limit())));
            buffer.clear();
        }
        if (stringBuilder.length() > 0) {
            System.out.println(stringBuilder);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        NioClient client = new NioClient();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            switch (input) {
                case "start":
                    client.start();
                    break;
                case "send":
                    client.sendMessage(scanner.nextLine());
                    break;
                case "read":
                    client.readChat();
                    break;
                case "stop":
                    client.stop();
                    break;
                default:
                    continue;
            }
        }
    }
}
