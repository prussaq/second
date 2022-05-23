package serialization;

import message.MessageClient;

import java.io.*;
import java.net.Socket;

public class SerlznClient {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final BufferedReader in;

    public SerlznClient(String ip, int port) throws IOException {
        System.out.println("Client connecting");
        socket = new Socket(ip, port);
        System.out.println("Connected: " + socket);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send() throws IOException {
        File file = new File("storage/io/src/main/resources/folder/test.txt");
        out.writeObject(file);
        System.out.println("Sent: " + file);

        System.out.println("Client reading");
        String resp = in.readLine();
        System.out.println("Response: " + resp);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SerlznClient client = new SerlznClient("127.0.0.1", 6666);
        client.send();
        client.stopConnection();
    }
}
