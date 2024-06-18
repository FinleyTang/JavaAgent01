import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileIOTest {
    public static void main(String[] args) {
        try {
            // 写入文件
            FileOutputStream fos = new FileOutputStream("testfile.txt");
            fos.write("Hello, world!".getBytes());
            fos.close();

            // 读取文件
            FileInputStream fis = new FileInputStream("testfile.txt");
            byte[] data = new byte[1024];
            int bytesRead = fis.read(data);
            System.out.println("Read " + bytesRead + " bytes.");
            fis.close();

            // 保持程序运行以便加载 Agent
            System.out.println("Waiting...");
            Thread.sleep(60000); // 等待 60 秒
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
