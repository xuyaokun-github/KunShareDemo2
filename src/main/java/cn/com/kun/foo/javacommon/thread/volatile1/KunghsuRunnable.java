package cn.com.kun.foo.javacommon.thread.volatile1;

/**
 * volatile保证可见性的例子
 *
 * author:xuyaokun_kzx
 * date:2024/2/7
 * desc:
*/
public class KunghsuRunnable implements Runnable {

    //加volatile，保证变量值能立刻写回主存，保证了线程间的可见性
//    volatile boolean quit = false;

    //不加volatile（线程1始终无法退出）
    boolean quit = false;

    int i = 0;

    @Override
    public void run() {
        while (!quit){

            //i++;

            //以下三种情况，都会促使线程1去主存刷新最新的值回工作内存

            //第一种情况：使用synchronized
            //加上sychronize同步锁 会产生锁竞争
            // 因为有出现锁，在程序指令上就会有把工作副本写回主存的过程，也会有从工作主存刷新获取最新值的过程
            // 所以这里能拿到最新的quit，所以线程能退出
//            synchronized (this){
//                i++;
//            }

            //第二种情况：使用System.out.println
            //和上面的情况是一样的，因为System.out.println底层源码就用到synchronized
//            i++;
//            System.out.println(i);

            //第三种情况：使用sleep
            //线程睡眠，sleep是阻塞线程并不释放锁，只是让出cpu调度。 让出cpu调度后下次执行会刷新工作内存
            //所以会把主存里的最新值读回来
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println(String.format("%s线程退出", Thread.currentThread().getName()));
    }

    public static void main(String[] args) throws InterruptedException {

        //生成一个Accounting自定义对象，这时对象是在主存中
        KunghsuRunnable runnable = new KunghsuRunnable();

        //线程1一直做i++
        //线程1会把Accounting自定义对象的内存值拷贝到工作内存中
        Thread a1 = new Thread(runnable, "a1");

        //线程2负责去改quit标志的值 只要为true,线程1就退出
        Thread a2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println("开始修改quit变量");
                    //线程2虽然通过set方法改了quit的值，但是并没有立刻写回主存，所以线程1见不到它的修改，所以线程1持续循环，线程1始终不退出
                    runnable.setQuit(true);
                    System.out.println("修改quit变量结束,set quit =true");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "a2");
        a1.start();
        a2.start();
        Thread.sleep(1000 * 10);
        System.out.println("main线程退出");
        System.out.println("启动线程3");
        Thread a3 = new Thread(new Runnable() {
            @Override
            public void run() {
                //线程3 读取的肯定是线程2改过的值，是最新的值
                System.out.println(String.format("%s线程退出，当前quit变量：%s", Thread.currentThread().getName(), runnable.isQuit()));
            }
        }, "a3");
        a3.start();

    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }
}
