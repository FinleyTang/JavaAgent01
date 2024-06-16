package mypackage;

public class MyClass {
    public static void main(String[] args) throws Exception {
        System.out.println("Executing command: ls -l");
        Process process = Runtime.getRuntime().exec("whoami");
        process.waitFor();
        System.out.println("Command executed.");
    }
}
