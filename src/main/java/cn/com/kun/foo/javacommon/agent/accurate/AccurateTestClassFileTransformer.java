package cn.com.kun.foo.javacommon.agent.accurate;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.SneakyThrows;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;

/**
 * -javaagent:D:\home\kunghsu\agent\agent.jar=method=cn.com.kun.controller.other.JavaAgentDemoController#testResultVo
 *
 * author:xuyaokun_kzx
 * date:2023/10/8
 * desc:
*/
public class AccurateTestClassFileTransformer implements ClassFileTransformer {

    private Map<String, Map<String, ClassMethodVO>> classMethodRelationMap;

    public AccurateTestClassFileTransformer(Map<String, Map<String, ClassMethodVO>> classMethodRelationMap) {
        this.classMethodRelationMap = classMethodRelationMap;
    }

    @SneakyThrows
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        //拿到的默认是/拼接的类名
        //替换成规范的全限定名
        String newClassName = className.replaceAll("/", ".");

//        if (className.contains("JavaAgentDemoController")){
//            System.out.println("transform方法拿到类名:" + className);
//            System.out.println("transform方法拿到类名(替换后):" + newClassName);
//            System.out.println("classMethodRelationMap内容:" + show(classMethodRelationMap));
//        }

        //通过类名快速匹配
        if (classMethodRelationMap.containsKey(newClassName)){
            System.out.println("开始处理class:" + newClassName);
            final ClassPool classPool = ClassPool.getDefault();
            //解决javassist.NotFoundException问题
            ClassClassPath classPath = new ClassClassPath(this.getClass());
            classPool.insertClassPath(classPath);
            //获取class对象封装类
            final CtClass clazz = classPool.get(newClassName);


            //待处理的方法
            Map<String, ClassMethodVO> methodVOMap = classMethodRelationMap.get(newClassName);

            methodVOMap.forEach((method, classMethodVO)->{
                try {
                    //拿到名为method的方法
                    CtMethod convertToAbbr = clazz.getDeclaredMethod(method);
                    convertToAbbr.getMethodInfo().toString();
                    CtClass ctClass = convertToAbbr.getReturnType();
                    ctClass.getClassFile();
                    ctClass.getClassFile2();
                    ctClass.getPackageName();
                    ctClass.getURL();
                    //重新设置方法体(TODO 进阶版，可以替换定制化方法内容)
//                    String methodBody = "return \"kunghsu2\";";
                    String methodBody = generateMethodBody(ctClass);
                    convertToAbbr.setBody(methodBody);
                    // 将新的class文件写到指定的目录，即可得到修改后的class文件
                    ctClass.writeFile("D:\\home\\kunghsu\\agent");
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

            // 返回字节码，并且detachCtClass对象
            byte[] byteCode = clazz.toBytecode();
            //detach的意思是将内存中曾经被javassist加载过的对象移除，如果下次有需要在内存中找不到会重新走javassist加载
            clazz.detach();
            return byteCode;

        }

        // 如果返回null则字节码不会被修改
        return null;
    }

    private String generateMethodBody(CtClass ctClass) {

        String simpleName = ctClass.getSimpleName();//简单类名
        String name = ctClass.getName();//返回类型的全限定名

        if ("String".contains(simpleName) || "java.lang.String".equals(name)){
            return "return \"AccurateTestMockString\";";
        }

        //假如是int\float\double\long等类型，return null会被强制转换为0

        //默认返回空
        return "return null;";
    }

    private String show(Map<String, Map<String, ClassMethodVO>> classMethodRelationMap) {

        StringBuilder builder = new StringBuilder();
        classMethodRelationMap.keySet().forEach(obj->{
            builder.append(obj).append(";");
        });
        return builder.toString();
    }

}
