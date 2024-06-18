package mypackage;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class FileWriteDemo {
    public static void main(String[] args) throws Exception {
        File file = new File("aaa.txt");
        String content = "Hello world.";
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}