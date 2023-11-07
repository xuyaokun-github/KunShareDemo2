package cn.com.kun.foo.javacommon.lambda.optional.demo1;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Optional链式调用例子
 *
 * author:xuyaokun_kzx
 * date:2023/11/7
 * desc:
*/
public class TestOptionalDemo1 {

    public static void main(String[] args) {


        //初始化数据
        OptDemoUser3 optDemoUser3 = new OptDemoUser3("kunghsu");
        OptDemoUser2 optDemoUser2 = new OptDemoUser2(optDemoUser3);
        OptDemoUser optDemoUser = new OptDemoUser();
        AtomicReference atomicReference = new AtomicReference<>();
        atomicReference.set(optDemoUser2);
        //注释掉其中一个环节的set操作，模拟一个空情况，结果会都输出default
        optDemoUser.setDemoUser2AtomicReference(atomicReference);
        Map<String, OptDemoUser> optDemoUserMap = new ConcurrentHashMap<>();
        optDemoUserMap.put("aaa", optDemoUser);

        //旧版代码
        System.out.println(oldMethod(optDemoUserMap));
        //新版代码
        System.out.println(newMethod(optDemoUserMap));


    }

    private static String newMethod(Map<String, OptDemoUser> optDemoUserMap) {

        String result = "default";
        return Optional.ofNullable(optDemoUserMap.get("aaa"))
                .map(OptDemoUser::getDemoUser2AtomicReference)
                .map(AtomicReference::get)
                .map(OptDemoUser2::getOptDemoUser3)
                .map(OptDemoUser3::getName)
                .orElse(result);
    }

    private static String oldMethod(Map<String, OptDemoUser> optDemoUserMap) {

        String result = "default";
        OptDemoUser optDemoUser = optDemoUserMap.get("aaa");
        if (optDemoUser != null){
            AtomicReference<OptDemoUser2> atomicReference = optDemoUser.getDemoUser2AtomicReference();
            if (atomicReference != null && atomicReference.get() != null){
                OptDemoUser2 optDemoUser2 = atomicReference.get();
                if (optDemoUser2 != null){
                    OptDemoUser3 optDemoUser3 = optDemoUser2.getOptDemoUser3();
                    if (optDemoUser3 != null){
//                        System.out.println("结果：" + optDemoUser3.getName());
                        result =  optDemoUser3.getName();
                    }
                }
            }
        }
        return result;
    }


}
