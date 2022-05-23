package file;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FileServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private InputStream in;

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        System.out.println("message.Server started on port: " + port);

        System.out.println("message.Server accepting");
        clientSocket = serverSocket.accept();
        System.out.println("Accepted " + clientSocket);

        System.out.println(clientSocket.getInputStream().getClass());

        out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
        in = clientSocket.getInputStream(); // use bufferedInputStream

        FileWriter fileWriter = new FileWriter("file$1");
        int read = 0;
        // use DataInputStream first to get file's length and read bytes until reach the length
        while ((read = in.read()) > 0) {
            System.out.println("read = " + read);
            fileWriter.write(read);
        }
        fileWriter.close();

        out.println("OK");
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws Exception {
        FileServer server = new FileServer();
        server.start(6666);
        server.stop();
    }
}
