package cn.com.kun.foo.javacommon.thread.volatile3;

public class Volatile3Test {

    public static void main(String[] args) throws InterruptedException {

        MyVolatile3Thread tt = new MyVolatile3Thread();
        tt.start();

        while (true) {
            //主线程尝试去修改这个MyVolatile2Thread对象里的Map，当添加10个元素之后，就加一个第100个元素
            /*
                假如主线程的这个修改动作对 其他线程可见的话，tt线程打印的内容将含有这个第100个元素
                事实证明，加了volatile之后，主线程这个修改变量的操作对线程tt是可见的，说明volatile有效果
             */
            if (tt.getMapData().size() == 10) {
                tt.setMapInnerDta(100,"100");
            }
        }
    }
}
