package cn.com.kun.foo.javacommon.collections.set;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class TestSetRemove {

    public static void main(String[] args) {

        Set<String> newSet = new HashSet<>();
        newSet.add("李四");
        newSet.add("王五");

//        Set<String> oldSet = new HashSet<>();
        //换成CopyOnWriteArraySet，可以解决ConcurrentModificationException
        Set<String> oldSet = new CopyOnWriteArraySet<>();
        oldSet.add("张三");
        oldSet.add("李四");
        oldSet.add("王五");

        System.out.println(oldSet);

        //反例
        // 此处会发生并发修改异常
        //会报错：Exception in thread "main" java.util.ConcurrentModificationException
        for(String str : oldSet) {
            if (!newSet.contains(str)) {
                oldSet.remove(str);
            }
        }


        //正确的删除方法
//        Iterator iterator = oldSet.iterator();
//        while(iterator.hasNext()) {
//            String s = (String) iterator.next();
//            if (!newSet.contains(s)) {
//                iterator.remove();
//            }
//        }
        System.out.println(oldSet);

    }
}
