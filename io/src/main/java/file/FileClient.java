package file;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FileClient {

    private Socket socket;
    private OutputStream out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        System.out.println("Client connecting");
        socket = new Socket(ip, port);
        System.out.println("Connected: " + socket);

        out = socket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    }

    public void send() throws IOException {
        System.out.println("Client sending a file");

        File file = new File("storage/io/src/main/resources/folder/test.txt");
        FileReader fileReader = new FileReader(file);
        int read;

        // use DataOutputStream first to send file's length
        while ((read = fileReader.read()) > 0) {
            out.write(read);
        }
        fileReader.close();

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
        FileClient client = new FileClient();
        client.startConnection("127.0.0.1", 6666);
        client.send();
        client.stopConnection();
    }
}
