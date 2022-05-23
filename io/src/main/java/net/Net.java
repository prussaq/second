package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Net {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        try (Socket socket = new Socket("www.google.com", 80)) {
            System.out.println("Socket connected: " + socket.isConnected());
            OutputStream outputStream = socket.getOutputStream();
            String msg = "GET / HTTP/1.1\nHost: www.google.com\n\n";
//            msg = "GET http://www.google.com/ \n\n";
            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
            InputStream inputStream = socket.getInputStream();
            int read = 0;
            while ((read = inputStream.read()) > 0) {
                System.out.print((char) read);
            }
        }
    }
}
