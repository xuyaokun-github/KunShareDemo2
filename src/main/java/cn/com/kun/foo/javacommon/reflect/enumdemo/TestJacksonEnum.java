package cn.com.kun.foo.javacommon.reflect.enumdemo;

import cn.com.kun.common.utils.JacksonUtils;

public class TestJacksonEnum {

    public static void main(String[] args) {


        MyDemoEnum myDemoEnum = MyDemoEnum.SUCCESS;
        String json = JacksonUtils.toJSONString(myDemoEnum);
        System.out.println(json);
        MyDemoEnum myDemoEnum2 = JacksonUtils.toJavaObject(json, MyDemoEnum.class);
        System.out.println(myDemoEnum2);
    }
}
