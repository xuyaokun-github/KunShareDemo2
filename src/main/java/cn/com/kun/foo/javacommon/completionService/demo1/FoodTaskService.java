package cn.com.kun.foo.javacommon.completionService.demo1;

import cn.com.kun.common.vo.ResultVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取食物列表服务类
 * @author Kunghsu
 * @datetime 2018年3月24日 下午1:17:00
 * @desc
 */
public class FoodTaskService {

    public ResultVo getFoodList(Map<String, String> requestMap){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("foodname", "name1");
        map1.put("fooddm", "dm1");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("foodname", "name2");
        map2.put("fooddm", "dm2");
        resultList.add(map1);
        resultList.add(map2);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("foodList", resultList);
        return ResultVo.valueOfSuccess(resultMap);
    }

}


