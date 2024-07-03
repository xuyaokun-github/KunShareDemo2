package cn.com.kun.controller.mybatis.multiDatasourceSwitch;


import cn.com.kun.bean.entity.User;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mybatis-multiDatasourceSwitch")
@RestController
public class MultiDatasourceSwitchDemoController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/changeToMain")
    public ResultVo changeToMain(){

        GlobalDataSourceLookupSaveContainer.changeDataSource("primary");
        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/changeToSecond")
    public ResultVo changeToSecond(){

        GlobalDataSourceLookupSaveContainer.changeDataSource("secondary");
        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/query")
    public ResultVo query(){


        User user = userMapper.getUserByFirstname("kunghsu0702");
        return ResultVo.valueOfSuccess(user);
    }

}
