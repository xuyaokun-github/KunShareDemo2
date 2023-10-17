package cn.com.kun.foo.javacommon.agent.accurate;


import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/10/8
 * desc:
*/
public class AccurateTestAgent {

    /**
     * method=cn.com.kunghsu.Test#method1,cn.com.kunghsu.Test#method2;
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {

        System.out.println("AccurateTestAgent invoke! agentArgs:" + agentArgs);

        try {
            //agent参数解析
            Map<String, String> argsMap = resloveArgsMap(agentArgs);
            Map<String, Map<String, ClassMethodVO>> classMethodRelationMap = resloveClassMethodRelationMap(argsMap);

            inst.addTransformer(new AccurateTestClassFileTransformer(classMethodRelationMap));
        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    private static Map<String, Map<String, ClassMethodVO>> resloveClassMethodRelationMap(Map<String, String> argsMap) {

        Map<String, Map<String, ClassMethodVO>> classMethodRelationMap =  new HashMap<>();
        //方法名解析
        if (argsMap.containsKey("method")){
            //
            String methods = argsMap.get("method");
            String[] methodArr = methods.split(",");
            if (methodArr != null && methodArr.length > 0){
                for (String methodString : methodArr){
                    String[] arr2 = methodString.split("#");
                    if (arr2 != null && arr2.length == 2){
                        ClassMethodVO classMethodVO = new ClassMethodVO();
                        classMethodVO.setClassName(arr2[0]);
                        classMethodVO.setMethodName(arr2[1]);
                        if (!classMethodRelationMap.containsKey(classMethodVO.getClassName())){
                            classMethodRelationMap.put(classMethodVO.getClassName(), new HashMap<>());
                        }
                        classMethodRelationMap.get(classMethodVO.getClassName()).put(classMethodVO.getMethodName(), classMethodVO);
                    }
                }
            }
        }
        return classMethodRelationMap;
    }

    private static Map<String, String> resloveArgsMap(String agentArgs) {

        Map<String, String> argsMap = new HashMap<>();
        String[] agentArgsArr = agentArgs.split(";");
        if (agentArgsArr != null && agentArgsArr.length > 0){
            for (String agentArgItem : agentArgsArr){
                String[] arr1 = agentArgItem.split("=");
                //method -> cn.com.kunghsu.Test#method1,cn.com.kunghsu.Test#method2
                argsMap.put(arr1[0], arr1[1]);
            }
        }
        return argsMap;
    }

    public static void main(String[] args) {
        System.out.println("AccurateTestAgent main invoke!");
        if (args != null){
            for (String arg : args){
                System.out.println("main方法接收到的参数：" + arg);
            }
        }
    }

}
