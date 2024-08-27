package cn.com.kun.springframework.core.jackson.mapdemo;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.HashMap;
import java.util.Map;

public class TestTryToMap {

    public static void main(String[] args) {


        Map<String, String> map = new HashMap<>();
        map.put("aaa", "123");
        String source = JacksonUtils.toJSONString(map);
        Map map1 =  JacksonUtils.tryToMap(source);
        System.out.println(map1);

    }
}
