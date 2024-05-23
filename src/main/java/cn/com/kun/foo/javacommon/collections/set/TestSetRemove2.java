package cn.com.kun.foo.javacommon.collections.set;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TestSetRemove2 {

    public static void main(String[] args) throws InterruptedException {

        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("111", "");
        map.put("222", "");
        map.put("333", "");

        Set set = map.keySet();

        //模拟一个异步线程，在set开始遍历之后，尝试往set中添加新内容
        new Thread(()->{
            //这是一个异步线程（它会做加操作）
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put("444", "");
            System.out.println("添加：" + "444");
            map.put("11", "");
            System.out.println("添加：" + "11");
            map.put("1111", "");
            System.out.println("添加：" + "1111");
            map.put("55", "");
            System.out.println("添加：" + "55");
            map.put("666", "");
            System.out.println("添加：" + "666");
            //为什么加 88，遍历时看不到，为什么加888，遍历时就看到了呢？ 这个和set的数据结构有关，说明steam它是有固定遍历顺序。
            map.put("88", "");
            System.out.println("添加：" + "88");
            map.put("888", "");
            System.out.println("添加：" + "888");
        }).start();

        set.stream().forEach(str->{
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            set.remove(str);
            //最终可以看到日志，输出的内容包含了新添加的元素，也就是说明，steam遍历的当前的实时内容
            System.out.println("删除：" + str);
        });

        System.out.println("遍历结束");
        //正确的删除方法
//        Iterator iterator = oldSet.iterator();
//        while(iterator.hasNext()) {
//            String s = (String) iterator.next();
//            if (!newSet.contains(s)) {
//                iterator.remove();
//            }
//        }
        Thread.sleep(3000);
        System.out.println(JacksonUtils.toJSONString(map));

    }
}
