## 运行步骤

编译：

```c
# 编译 MyClass.java
cd MyProject
javac -d . mypackage/MyClass.java
 编译 CommandInterceptorAgent.java，并包括 javassist 库
javac -cp .:lib/javassist-3.28.0-GA.jar CommandInterceptorAgent.java
```

打包：

```shell
jar cfm CommandInterceptorAgent.jar MANIFEST.MF CommandInterceptorAgent.class
```

运行：
```shell
java -javaagent:CommandInterceptorAgent.jar -cp .:lib/javassist-3.28.0-GA.jar mypackage.MyClass
```
