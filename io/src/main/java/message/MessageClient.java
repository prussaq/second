package message;

import java.io.*;
import java.net.Socket;

public class MessageClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        System.out.println("Client connecting");
        socket = new Socket(ip, port);
        System.out.println("Connected: " + socket);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // use charset
    }

    public void send() throws IOException {
        final String message = "Server, server!\nIt's a client";
        out.println(message);
        System.out.println("Sent: " + message);
        out.println("null");
//        out.println((String) null); // A terminal sign for server's reader stream (won't be used as text)
//        out.close(); // Closing stream closes socket

        System.out.println("Client reading");
        String resp = in.readLine();
        System.out.println("Response: " + resp);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        MessageClient client = new MessageClient();
        client.startConnection("127.0.0.1", 6666);
        client.send();
        client.stopConnection();
    }
}
