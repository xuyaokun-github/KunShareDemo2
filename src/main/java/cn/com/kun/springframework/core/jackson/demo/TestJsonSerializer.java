package cn.com.kun.springframework.core.jackson.demo;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.core.jackson.lanyang.LanYangVO;

public class TestJsonSerializer {

    public static void main(String[] args) {

//        JsonSerializer jsonSerializer = null;

        User user = new User();
        user.setFirstname("kunghsu");
        user.setUsername("123456");
        System.out.println(JacksonUtils.toJSONString(user));

        LanYangVO lanYangVO = new LanYangVO();
        lanYangVO.setName("kunghsu");
        System.out.println(JacksonUtils.toJSONString(lanYangVO));

    }
}
