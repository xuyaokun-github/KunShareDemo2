package cn.com.kun.component.twiceaccess.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 二次访问组件 异步线程池
 *
 * author:xuyaokun_kzx
 * date:2023/11/21
 * desc:
*/
public class TwiceAccessExecutor {

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void execAsync(Runnable runnable){

        executorService.submit(runnable);
    }


}
