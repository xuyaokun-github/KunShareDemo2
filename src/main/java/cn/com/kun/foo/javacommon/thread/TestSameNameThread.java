package cn.com.kun.foo.javacommon.thread;

/**
 * Created by xuyaokun
 * @desc:
 */
public class TestSameNameThread {

    public static void main(String[] args) throws Exception {
        new Thread(()->{
            while(true){
                try {
                    Thread.sleep(2 * 1000);
                    System.out.println("1当前线程:" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(()->{
            while(true){
                try {
                    Thread.sleep(2 * 1000);
                    System.out.println("2当前线程:" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
    }
}
