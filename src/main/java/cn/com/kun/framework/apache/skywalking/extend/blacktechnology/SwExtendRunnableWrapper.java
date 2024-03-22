package cn.com.kun.framework.apache.skywalking.extend.blacktechnology;

/**
 * skywalking黑科技
 *
 * author:xuyaokun_kzx
 * date:2024/3/21
 * desc:
*/
public class SwExtendRunnableWrapper implements Runnable {
    final Runnable runnable;

    public SwExtendRunnableWrapper(Runnable runnable) {
        this.runnable = runnable;
    }

    public static SwExtendRunnable of(Runnable r) {
        return new SwExtendRunnable(r);
    }

    public void run() {
        this.runnable.run();
    }
}
