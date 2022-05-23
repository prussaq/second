package serialization;

import message.MessageServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SerlznServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedInputStream in;

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port: " + port);
        System.out.println("Server accepting");
        clientSocket = serverSocket.accept();
        System.out.println("Accepted " + clientSocket);

        in = new BufferedInputStream(clientSocket.getInputStream());
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        ObjectInputStream ois = new ObjectInputStream(in);
        File file = (File) ois.readObject();
        System.out.println("file = " + file);
        long length = file.length();
        System.out.println("length = " + length);
        boolean exists = file.exists();
        System.out.println("exists = " + exists);
        boolean mkdirs = file.getParentFile().mkdirs();
        System.out.println("mkdirs = " + mkdirs);
        boolean newFile = file.createNewFile(); // creating but no content
        System.out.println("newFile = " + newFile);

        System.out.println("Server responding");
        out.println("Copy! Server.");
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws Exception {
        SerlznServer server = new SerlznServer();
        server.start(6666);
        server.stop();
    }
}
