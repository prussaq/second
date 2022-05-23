package message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port: " + port);
        System.out.println("Server accepting");
        clientSocket = serverSocket.accept();
        System.out.println("Accepted " + clientSocket);

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // use charset

        // receiving by chars (won't stop the loop)
//        InputStream inputStream = clientSocket.getInputStream();
//        int read;
//        while ((read = inputStream.read()) != -1) {
//            System.out.println("read: " + read + " char: " + (char) read);
//        }

        // using special string to stop but can't use it as text message
        String message;
        while (!(message = in.readLine()).equals("null")) {
            System.out.println("Read: " + message);
        }

        System.out.println("Server responding");
//        out.print("Copy! Server."); // no adding a \n to the end (client won't get the message)
        out.println("Copy! Server.");
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws Exception {
        MessageServer server = new MessageServer();
        server.start(6666);
        server.stop();
    }
}
