package cn.com.kun.springframework.core.jackson.jacksonModuleParameterNamesDemo;

import cn.com.kun.common.utils.JacksonUtils;

public class TestJacksonModuleParameterNamesDemo {

    public static void main(String[] args) {

        JmpnDemoVO jmpnDemoVO = new JmpnDemoVO("kunghsu");
        String json = JacksonUtils.toJSONString(jmpnDemoVO);
        JmpnDemoVO newJmpnDemoVO = JacksonUtils.toJavaObject(json, JmpnDemoVO.class);
        System.out.println(newJmpnDemoVO);
    }


}
