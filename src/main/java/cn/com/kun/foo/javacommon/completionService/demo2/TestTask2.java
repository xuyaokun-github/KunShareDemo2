package cn.com.kun.foo.javacommon.completionService.demo2;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.foo.javacommon.completionService.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestTask2 {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(UUID.randomUUID().toString());
        }

        //原逻辑
//        for (String str : list){
//            method(str);
//        }
        //新逻辑
        List<Runnable> runnableList = new ArrayList<>();
        for (String str : list){
            runnableList.add(()->{
                method(str);
            });
        }
        ThreadPoolUtil.submitTasks(runnableList);
//        for (int i = 0; i < 1000000; i++) {
//            ThreadPoolUtil.submitTasks(runnableList);
//            System.out.println(i);
//        }
        ThreadUtils.sleep(60 * 1000);
    }

    private static void method(String str) {

        ThreadUtils.sleep(10000);
        System.out.println("执行方法:" + str);
    }


}
