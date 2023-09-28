package cn.com.kun.foo.javacommon.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.SneakyThrows;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyClassFileTransformer implements ClassFileTransformer {

    @SneakyThrows
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        //通过类名快速匹配
        if (className.contains("JavaAgentDemoController")){
            final ClassPool classPool = ClassPool.getDefault();
            final CtClass clazz = classPool.get("cn.com.kun.controller.other.JavaAgentDemoController");
            //拿到名为testString的方法
            CtMethod convertToAbbr = clazz.getDeclaredMethod("testString");
            //重新设置方法体
            String methodBody = "return \"kunghsu2\";";
            convertToAbbr.setBody(methodBody);
            // 返回字节码，并且detachCtClass对象
            byte[] byteCode = clazz.toBytecode();
            //detach的意思是将内存中曾经被javassist加载过的对象移除，如果下次有需要在内存中找不到会重新走javassist加载
            clazz.detach();
            return byteCode;
        }
        // 如果返回null则字节码不会被修改
        return null;
    }

}
