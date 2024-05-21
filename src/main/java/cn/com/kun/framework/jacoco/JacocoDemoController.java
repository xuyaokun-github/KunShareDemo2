package cn.com.kun.framework.jacoco;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.mybatis.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/jacoco-demo")
@RestController
public class JacocoDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(JacocoDemoController.class);

    @Autowired
    private UserService userService;


    @PostMapping("/test")
    public ResultVo testBomEncodingForPost(@RequestBody String firstname) throws IOException {


//        User user = userService.queryUserByFirstName(firstname);
        User user = new User();
        String userStirng = JacksonUtils.toJSONString(user);
        LOGGER.info("firstname:{}", firstname);

        return ResultVo.valueOfSuccess("OK");
    }

}
