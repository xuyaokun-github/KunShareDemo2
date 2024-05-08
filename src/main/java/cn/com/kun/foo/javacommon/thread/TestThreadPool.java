package cn.com.kun.foo.javacommon.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {

    private static ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(2, 2, 60, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1));


    public static void main(String[] args) {

        int count = 100;
//        int count = 1;

        for (int i = 0; i < count; i++) {
            System.out.println(i);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < 2; j++) {
                threadPoolExecutor.execute(()->{
                    int a = 0;
                });
            }
        }
        System.out.println("执行完成了");
        threadPoolExecutor.shutdown();
    }
}
