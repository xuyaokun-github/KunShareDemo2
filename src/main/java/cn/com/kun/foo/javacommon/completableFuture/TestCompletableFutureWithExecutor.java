package cn.com.kun.foo.javacommon.completableFuture;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.foo.javacommon.completionService.ConcurrenceTaskUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 关于CompletableFuture和线程池一起使用的一些正例反例代码
 *
 * author:xuyaokun_kzx
 * date:2024/10/24
 * desc:
*/
public class TestCompletableFutureWithExecutor {

    private static Executor executor = Executors.newFixedThreadPool(10);

    private static ExecutorService FixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000 * 100; i++) {
            list.add(i + "-" + UUID.randomUUID().toString());
        }

//        test1();
//        test2();

        //正解
//        test3(list);

        //用CompletionService的方式实现test3方法的效果
        test4(list);

    }

    private static void test4(List<String> list) {

        List<Runnable> runnableList = new ArrayList<>();
        for (String str : list){
            System.out.println("添加str:" + str);
            runnableList.add(()->{
                //业务逻辑
                System.out.println(str + " " + Thread.currentThread().getName());
                ThreadUtils.sleep(3000);
            });
            if (runnableList.size() > 1000){
                //提交任务
                ConcurrenceTaskUtil.submitTasks(runnableList, FixedThreadPool);
                runnableList.clear();
            }
        }


    }

    private static void test3(List<String> list) {

        List<CompletableFuture> completableFutureList = new ArrayList<>();
        for (String str : list){
            System.out.println("添加str:" + str);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                //业务逻辑
                System.out.println(str + " " + Thread.currentThread().getName());
                ThreadUtils.sleep(3000);
            }, executor);
            completableFutureList.add(future);
            if (completableFutureList.size() > 1000){
                for (int i = 0; i < completableFutureList.size(); i++) {
                    //正解（但是一旦用这个，代码就等于退化回单线程了）
                    try {
                        completableFutureList.get(i).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                completableFutureList.clear();
            }
        }

    }

    /**
     * 这也是反例代码，想用多线程，但是最终退化回单线程了
     * 正例代码，请参考test3
     */
    private static void test2() {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000 * 100; i++) {
            list.add(i + "-" + UUID.randomUUID().toString());
        }

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            list.stream().forEach(str->{
                System.out.println("添加str:" + str);
                System.out.println(str + " " + Thread.currentThread().getName());
                ThreadUtils.sleep(3000);
            });
        }, executor);

        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
        这是一个经典的反例代码
        一直往线程池丢内容，有可能会造成内存溢出
    */
    private static void test1() {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000 * 100; i++) {
            list.add(i + "-" + UUID.randomUUID().toString());
        }

        for (String str : list){
            System.out.println("添加str:" + str);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println(str + " " + Thread.currentThread().getName());
                ThreadUtils.sleep(3000);
            }, executor);

            //正解（但是一旦用这个，代码就等于退化回单线程了）
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


}
