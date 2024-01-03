package cn.com.kun.foo.javacommon.datatype;

import java.time.Duration;

public class TestInt {

    public static void main(String[] args) {

        int a = 10;
        int b = 9;
        //左边 比 右边 大，就是 大于0
        System.out.println(Duration.ofMillis(10).compareTo(Duration.ofMillis(9)) < 0);

    }
}
