import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
//        File file = new File("storage/io/src/main/resources/folder");
//        Arrays.stream(file.list()).forEach(System.out::println);


        URL systemResource = ClassLoader.getSystemResource("folder/test.txt");
        File file = new File(systemResource.toURI());
        Socket socket = new Socket();
        OutputStream out = socket.getOutputStream();
        System.out.println(out.getClass());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);
        DataOutputStream dataOutputStream = new DataOutputStream(out);

//        try (InputStream in = new FileInputStream(file)) {
//            int read = 0;
//            while ((read = in.read()) > 0) {
//                out.write(read);
//            }
//        }
//
//        System.out.println("file = " + file);
//        Files.readAllLines(file.toPath()).forEach(System.out::println);
    }
}
