import javassist.*;
import java.lang.instrument.*;
import java.security.ProtectionDomain;



public class CommandInterceptorAgent implements ClassFileTransformer {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new CommandInterceptorAgent());
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        CommandInterceptorAgent agent = new CommandInterceptorAgent();
        inst.addTransformer(agent, true);
        System.out.println("Agent attached.");

        // Retransform already loaded classes
        try {
            Class<?>[] loadedClasses = inst.getAllLoadedClasses();
            for (Class<?> clazz : loadedClasses) {
                if (inst.isModifiableClass(clazz) && (clazz.getName().equals("java.io.FileInputStream") || clazz.getName().equals("java.io.FileOutputStream"))) {
                    inst.retransformClasses(clazz);
                }
            }
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }
    }


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        System.out.println("Loaded class: " + className);  
        // return transformClass(classfileBuffer);
        if (className.equals("java/lang/ProcessImpl")|| className.equals("java/lang/UNIXprocess") || className.equals("java/io/FileInputStream") || className.startsWith("java/io/FileOutputStream")) {
            System.out.println("catched class: " + className+"\n");
            return transformClass(classfileBuffer);
        }
        return classfileBuffer;
    }

    private byte[] transformClass(byte[] classfileBuffer) {
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));

            // Find all methods named 'start' in java.lang.ProcessImpl
            // CtMethod[] methods = cc.getDeclaredMethods("start");
            CtMethod[] methods = cc.getDeclaredMethods();
            for (CtMethod method : methods) {
                // Insert code at the beginning of the method
                // method.insertBefore(getInsertionCode());
                    if (method.getName().equals("start")) {
                        method.insertBefore(getInsertionCode());
                    } else {
                        // System.out.println(method.getName());
                        // method.insertBefore(getInsertionFileRead());
                    }
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
    private String getInsertionFileRead() {
        return "{" +
               "    System.out.println(\"Intercepted command execution\");";
    }
}
