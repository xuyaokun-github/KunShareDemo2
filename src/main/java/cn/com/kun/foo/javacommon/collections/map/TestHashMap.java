package cn.com.kun.foo.javacommon.collections.map;

import java.util.HashMap;
import java.util.Map;

public class TestHashMap {

    public static void main(String[] args) {

//        main1();
//        main2();


        Map<String, Object> map = new HashMap<>();
//        map.put("kunghsu", 555);
        map.put("kunghsu", "");

        Integer integer = (Integer) map.get("kunghsu");
        System.out.println(integer);
        if (999 > integer){
            System.out.println("大于");
        }else {
            System.out.println("小于");
        }
    }

    private static void main1() {
        int MAXIMUM_CAPACITY = 1 << 30;

        int cap = 50;
        int n = cap - 1;
        System.out.println(n);
        n |= n >>> 1;
        System.out.println(n);
        n |= n >>> 2;
        System.out.println(n);
        n |= n >>> 4;
        System.out.println(n);
        n |= n >>> 8;
        System.out.println(n);
        n |= n >>> 16;
        System.out.println(n);
        int res =  (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
        System.out.println(res);
    }

    private static void main2() {
        int MAXIMUM_CAPACITY = 1 << 30;

        int cap = 50;
        int n = cap - 1;
        System.out.println(n);
        n |= n >>> 1;
        System.out.println(n);
        n |= n >>> 1;
        System.out.println(n);
        n |= n >>> 5;
        System.out.println(n);
        n |= n >>> 7;
        System.out.println(n);
        n |= n >>> 17;
        System.out.println(n);
        int res =  (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
        System.out.println(res);
    }

}
