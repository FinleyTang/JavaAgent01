package mypackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReadDemo {

    public static void main(String[] args) {
        // 指定文件路径
        String filePath = "test.txt";

        try {
            // 根据文件路径创建File对象
            File file = new File(filePath);
            // 确保文件存在
            if (!file.exists()) {
                System.out.println("文件不存在: " + filePath);
                return;
            }

            // 创建FileInputStream对象来读取文件
            FileInputStream in = new FileInputStream(file);
            int tempbyte;

            // 读取文件内容并打印到控制台
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }

            // 关闭输入流
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
