package cn.com.kun.springframework.core.jackson.jacksonModuleParameterNamesDemo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/9/2
 * desc:
*/
public class JmpnDemoVOMixin {

    /**
     * 模仿 第三方包里的类的构造函数，写一个空方法即可
     * @param name
     */
    @JsonCreator
    public JmpnDemoVOMixin(@JsonProperty("name") String name) {

    }

}

