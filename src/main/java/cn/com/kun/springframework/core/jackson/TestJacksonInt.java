package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.JacksonUtils;

public class TestJacksonInt {

    public static void main(String[] args) {

        Integer integer = new Integer(10);
        System.out.println(JacksonUtils.toJSONString(integer));
    }
}
