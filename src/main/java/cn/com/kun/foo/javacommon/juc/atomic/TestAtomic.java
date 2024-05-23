package cn.com.kun.foo.javacommon.juc.atomic;

import java.util.concurrent.atomic.AtomicLong;

public class TestAtomic {

    public static void main(String[] args) {


        AtomicLong atomicLong = new AtomicLong();

        System.out.println(atomicLong.get());

//        AtomicLong atomicLong = new AtomicLong(1000);

        long res = atomicLong.getAndSet(-500);
        System.out.println(res);

        System.out.println(Long.MAX_VALUE);
        System.out.println(0-Long.MAX_VALUE);
        System.out.println(Long.MIN_VALUE);

    }


}
