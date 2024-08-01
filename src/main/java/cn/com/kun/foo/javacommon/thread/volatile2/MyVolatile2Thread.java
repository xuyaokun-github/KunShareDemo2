package cn.com.kun.foo.javacommon.thread.volatile2;

import java.util.Map;
import java.util.TreeMap;

/**
 * volatile对Map变量本身修改是有效果的
 *
 * author:xuyaokun_kzx
 * date:2024/7/29
 * desc:
*/
public class MyVolatile2Thread extends Thread {

    /**
     * 不加volatile
     */
    private Map<Integer, String> mapData = new TreeMap<>();

    /**
     * 加了volatile
     */
//    private volatile Map<Integer, String> mapData = new TreeMap<>();

    public Map<Integer, String> getMapData() {
        return mapData;
    }

    public void setMapDta(Map<Integer, String> mapData) {
        this.mapData = mapData;
    }

    public void setMapInnerDta(int k, String v) {
        this.mapData.put(k, v);
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            System.out.println(mapData);
            mapData.put(++count, "" + count);
            try {
                //每隔一秒加一个元素，每次都打印整个map
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
