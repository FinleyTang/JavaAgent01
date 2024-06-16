import javassist.*;
import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class CommandInterceptorAgent implements ClassFileTransformer {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new CommandInterceptorAgent());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {

        if (className.equals("java/lang/ProcessImpl")) {
            return transformClass(classfileBuffer);
        }
        return classfileBuffer;
    }

    private byte[] transformClass(byte[] classfileBuffer) {
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));

            // Find all methods named 'start' in java.lang.ProcessImpl
            CtMethod[] methods = cc.getDeclaredMethods("start");
            for (CtMethod method : methods) {
                // Insert code at the beginning of the method
                method.insertBefore(getInsertionCode());
            }

            return cc.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getInsertionCode() {
        return "{" +
               "    System.out.println(\"Intercepted command execution\");" +
               "    if ($args != null && $args.length > 0 && $args[0] instanceof String[]) {" +
               "        String[] cmd = (String[]) $args[0];" +
               "        System.out.println(\"Command: \" + java.util.Arrays.toString(cmd));" +
               "    }" +
               "    System.out.println(\"Call stack:\");" +
               "    java.lang.StackTraceElement[] stackTrace = java.lang.Thread.currentThread().getStackTrace();" +
               "    for (int i = 1; i < stackTrace.length; i++) {" +
               "        System.out.println(\"    \" + stackTrace[i]);" +
               "    }" +
               "}";
    }
}
