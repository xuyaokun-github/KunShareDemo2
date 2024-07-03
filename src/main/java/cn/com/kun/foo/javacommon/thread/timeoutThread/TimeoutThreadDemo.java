package cn.com.kun.foo.javacommon.thread.timeoutThread;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;

import java.util.concurrent.*;

/**
 * 超时线程demo
 *
 * author:xuyaokun_kzx
 * date:2024/6/17
 * desc:
*/
public class TimeoutThreadDemo {

    public static void main(String[] args) {

        ReturnT executeResult = null;
        Thread futureThread = null;
        try {
            FutureTask<ReturnT<String>> futureTask = new FutureTask<ReturnT<String>>(new Callable<ReturnT<String>>() {
                @Override
                public ReturnT<String> call() throws Exception {

                    long start = System.currentTimeMillis();
                    while (true){
                        if (System.currentTimeMillis() - start > 10 * 1000){
                            break;
                        }
//                        Thread.sleep(1000);
                        System.out.println("后台任务执行中");
                    }

                    return ReturnT.SUCCESS;
                }
            });
            futureThread = new Thread(futureTask);
            futureThread.start();

            executeResult = futureTask.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
            executeResult = new ReturnT<String>(IJobHandler.FAIL_TIMEOUT.getCode(), "job execute timeout ");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //执行线程中断
            futureThread.interrupt();
        }


        System.out.println("主线程结束");
        System.out.println("结果：" + executeResult.getCode());

        try {
            futureThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            while (true){
                System.out.println("other thread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
