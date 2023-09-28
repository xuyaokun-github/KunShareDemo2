package cn.com.kun.foo.javacommon.agent;

import java.lang.instrument.Instrumentation;

/**
 * 需要将该类打成jar包
 *
 * author:xuyaokun_kzx
 * date:2023/9/28
 * desc:
*/
public class BeforeJvmAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain invoke!");
        inst.addTransformer(new MyClassFileTransformer());
    }

    public static void main(String[] args) {
        System.out.println("BeforeJvmAgent main invoke!");
    }
}
