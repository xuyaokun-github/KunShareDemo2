package cn.com.kun.foo.javacommon.juc;

import cn.com.kun.common.utils.ThreadUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestCondition {

    private ReentrantLock lock = new ReentrantLock();

    private Condition notEmptyCondition = lock.newCondition();

    public static void main(String[] args) {

        TestCondition testCondition = new TestCondition();

        new Thread(()->{
            testCondition.doB();
        }, "B").start();

        new Thread(()->{
            testCondition.doA();
//            Thread.yield();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "A").start();



        ThreadUtils.runForever();
    }

    private void doA() {
        while (true){
            try {
                lock.lock();
                System.out.println("执行A逻辑");
                notEmptyCondition.signalAll();
                Thread.sleep(3000);
                System.out.println("A逻辑发送通知");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }

    private void doB() {
        while (true){
            try {
                lock.lock();
                System.out.println("执行B逻辑");
                Thread.sleep(3000);
                notEmptyCondition.await();
                System.out.println("B逻辑阻塞结束");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }




}
