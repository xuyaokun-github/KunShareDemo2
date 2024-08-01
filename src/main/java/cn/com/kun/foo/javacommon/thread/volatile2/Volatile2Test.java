package cn.com.kun.foo.javacommon.thread.volatile2;

import java.util.TreeMap;

public class Volatile2Test {

    public static void main(String[] args) throws InterruptedException {

        MyVolatile2Thread tt = new MyVolatile2Thread();
        tt.start();

        while (true) {
            //主线程尝试去修改这个MyVolatile2Thread对象里的Map，每当元素到10个就重置
            /*
                假如主线程的这个修改动作对 其他线程可见的话，tt线程打印的内容不可能会超出10
                事实证明，加了volatile之后，主线程这个修改变量的操作对线程tt是可见的，说明volatile有效果
             */
            if (tt.getMapData().size() >= 10) {
                tt.setMapDta(new TreeMap<>());
                System.out.println("修改了map引用");
                Thread.sleep(2000);
            }


            //假如不加这一行，CPU会拉满，一直都不尝试去拿 tt.getMapData().size()的最新值
            //始终没法打印上面的那句“System.out.println("修改了map引用");”
//            Thread.sleep(50);

        }
    }
}
