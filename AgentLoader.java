import com.sun.tools.attach.VirtualMachine;

public class AgentLoader {
    public static void main(String[] args) {
        try {
            // 获取当前运行的 JVM 实例
            VirtualMachine vm = VirtualMachine.attach(args[0]);
            // 动态加载 Java Agent
            vm.loadAgent("CommandInterceptorAgent.jar");
            vm.detach();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
