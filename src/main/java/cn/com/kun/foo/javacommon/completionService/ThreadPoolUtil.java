package cn.com.kun.foo.javacommon.completionService;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ExceptionUtil;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 通用的线程池工具类（大家使用同一个池）
 *
 * @author Kunghsu
 * @datetime 2018年3月21日 下午9:01:42
 * @desc
 */
public class ThreadPoolUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadPoolUtil.class);

    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    public static ExecutorService cachedThreadPool2 = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()*3);

    /**
     * 入参是一系列线程任务
     * 出参是返回结果列表
     * @param tasks 推荐用ResultVo来做返回值实体
     * @return
     */
    public static List<Object> executeTasks(List<Callable<Object>> tasks) {

        if (tasks == null || tasks.isEmpty()) {
            //假如线程任务列表为空，不需要执行，直接返回空
            return null;
        }
        int num = tasks.size();
        //定义返回结果列表
        List<Object> results = new ArrayList<Object>(num);
        //根据线程池构造执行器
        CompletionService<Object> completionService = new ExecutorCompletionService<Object>(cachedThreadPool);
        for (Callable<Object> task : tasks) {
            //往线程池追加线程任务
            completionService.submit(task);
        }
        //循环获取结果（注意结果的顺序并不一定和提交任务的顺序一致）
        for (int i = 0; i < num; i ++) {
            try {
                results.add(completionService.take().get());
            } catch (InterruptedException e) {
                results.add(ResultVo.valueOfError("出现InterruptedException，具体原因：" + ExceptionUtil.getMessage(e)));
            } catch (ExecutionException e) {
                results.add(ResultVo.valueOfError("出现ExecutionException，具体原因：" + ExceptionUtil.getMessage(e)));
            }
        }
        return results;
    }

    /**
     * 初版，用了CountDownLatch，其实是多余的
     * @param tasks
     */
    public static void submitTasks2(List<Runnable> tasks) {

        if (tasks == null || tasks.isEmpty()) {
            //假如线程任务列表为空，不需要执行，直接返回空
            return ;
        }
        int num = tasks.size();
        CountDownLatch countDownLatch = new CountDownLatch(num);
        //定义返回结果列表
        List<Runnable> newTasks = new ArrayList<>(num);
        for (Runnable runnable : tasks){
            newTasks.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    }catch (Exception e){
                        LOGGER.error("并发任务执行异常", e);
                    }finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        //根据线程池构造执行器
        CompletionService<Object> completionService = new ExecutorCompletionService<Object>(cachedThreadPool2);
        for (Runnable task : newTasks) {
            //往线程池追加线程任务
            System.out.println("开始submit " + DateUtils.nowWithMillis());
            completionService.submit(task, null);
        }
        for (int i = 0; i < newTasks.size(); i++) {
            try {
                System.out.println("开始take " + DateUtils.nowWithMillis());
                Future future = completionService.take();
                System.out.println("开始get " + DateUtils.nowWithMillis());
                future.get();
                System.out.println("get方法执行结束 " + DateUtils.nowWithMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("开始进入await " + DateUtils.nowWithMillis());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("countDownLatch等待出现中断", e);
        }
        System.out.println("并发任务执行结束");
    }

    /**
     *
     * @param tasks
     */
    public static void submitTasks(List<Runnable> tasks) {

        if (tasks == null || tasks.isEmpty()) {
            //假如线程任务列表为空，不需要执行，直接返回空
            return ;
        }
        int num = tasks.size();
        //定义返回结果列表
        List<Runnable> newTasks = new ArrayList<>(num);
        for (Runnable runnable : tasks){
            newTasks.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    }catch (Exception e){
                        LOGGER.error("并发任务执行异常", e);
                    }
                }
            });
        }
        //根据线程池构造执行器
        CompletionService<Object> completionService = new ExecutorCompletionService<Object>(cachedThreadPool2);
        for (Runnable task : newTasks) {
            //往线程池追加线程任务
            completionService.submit(task, null);
        }
        //这里必须循环，将全部结果取完，一方面是为了清空内存对象，一方面是为了等待全部task完成
        for (int i = 0; i < newTasks.size(); i++) {
            try {
                Future future = completionService.take();
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("并发任务执行结束");
    }
}
