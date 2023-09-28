package cn.com.kun.foo.javacommon.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/9/28
 * desc:
*/
public class AfterJvmAgent {

    public static void agentmain(String agentArgs, Instrumentation inst)
            throws ClassNotFoundException, UnmodifiableClassException {

        inst.addTransformer(new MyClassFileTransformer(), true);
        // 关键点
        inst.retransformClasses(Class.forName("cn.com.kun.controller.other.JavaAgentDemoController",false,
                ClassLoader.getSystemClassLoader()));
    }

    public static void main(String[] args) {

    }

}
