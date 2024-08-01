package cn.com.kun.foo.javacommon.thread.volatile2;

import java.util.TreeMap;

/**
 * 验证一个线程假如退出了，它会将它修改的内容刷新回主存
 *
 * author:xuyaokun_kzx
 * date:2024/7/29
 * desc:
*/
public class Volatile2Test2 {

    public static void main(String[] args) throws InterruptedException {

        //异步线程1
        MyVolatile2Thread tt = new MyVolatile2Thread();
        tt.start();

        //异步线程2
        new Thread(()->{
            while (true) {
                //主线程尝试去修改这个MyVolatile2Thread对象里的Map，每当元素到10个就重置
            /*
                假如主线程的这个修改动作对 其他线程可见的话，tt线程打印的内容不可能会超出10
                事实证明，加了volatile之后，主线程这个修改变量的操作对线程tt是可见的，说明volatile有效果
             */
                if (tt.getMapData().size() == 10) {
                    //tt.setMapInnerDta(100,"100");
                    tt.setMapDta(new TreeMap<>());
                    break;
                }
            }
        }).start();

        while (true){

            //这个是主线程
        }
    }
}
