package cn.com.kun.foo.javacommon.completionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 通用的并发任务工具类（大家使用同一个池）
 *
 * @author Kunghsu
 * @datetime 2018年3月21日 下午9:01:42
 * @desc
 */
public class ConcurrenceTaskUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConcurrenceTaskUtil.class);

    /**
     * 共用线程池，执行一些小任务（假如是大批量任务，建议用特定线程池，这样才能实现业务隔离）
     */
    public static ExecutorService FixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    public static void submitTasks(List<Runnable> tasks){
        submitTasks(tasks, FixedThreadPool);
    }

    /**
     *
     * @param tasks
     */
    public static void submitTasks(List<Runnable> tasks, ExecutorService executorService) {

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
        CompletionService<Object> completionService = new ExecutorCompletionService<Object>(executorService);
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
                LOGGER.error("并发任务InterruptedException", e);
            } catch (ExecutionException e) {
                LOGGER.error("并发任务ExecutionException", e);
            }
        }
        LOGGER.info("并发任务执行结束");
    }
}
