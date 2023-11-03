package cn.com.kun.foo.javacommon.completionService.demo1;


import cn.com.kun.common.vo.ResultVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取用户列表服务类
 * @author Kunghsu
 * @datetime 2018年3月24日 下午1:17:00
 * @desc
 */
public class UserTaskService {

    public ResultVo getUserList(Map<String, String> requestMap){

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("username", "name1");
        map1.put("userdm", "dm1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("username", "name2");
        map2.put("userdm", "dm2");
        resultList.add(map1);
        resultList.add(map2);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("userList", resultList);
        return ResultVo.valueOfSuccess(resultMap);
    }

}
