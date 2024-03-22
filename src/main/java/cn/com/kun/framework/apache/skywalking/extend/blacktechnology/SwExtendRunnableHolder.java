package cn.com.kun.framework.apache.skywalking.extend.blacktechnology;

public class SwExtendRunnableHolder {

    private static ThreadLocal<Runnable> runnableThreadLocal = new InheritableThreadLocal<Runnable>();


    public static void set(Runnable runnable) {

        runnableThreadLocal.set(runnable);
    }


    public static Runnable get() {

        return runnableThreadLocal.get();
    }
}
