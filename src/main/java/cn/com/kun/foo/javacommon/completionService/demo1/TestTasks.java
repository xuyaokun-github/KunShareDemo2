package cn.com.kun.foo.javacommon.completionService.demo1;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.foo.javacommon.completionService.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
/**
 * 基于线程池并发处理
 * @author Kunghsu
 *
 */
public class TestTasks {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        //假如有一个前端界面，要展示用户列表，菜单列表，食物列表
        //获取这三类信息，我们可以并发去获取
        List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();

        //入参
        Map<String, String> requestMap = new HashMap<String, String>();

        tasks.add(new Callable<Object>(){//
            @Override
            public Object call() throws Exception {
                FoodTaskService foodTaskService = new FoodTaskService();
                return foodTaskService.getFoodList(requestMap);
            }
        });

        tasks.add(new Callable<Object>(){//
            @Override
            public Object call() throws Exception {
                MenuTaskService menuTaskService = new MenuTaskService();
                return menuTaskService.getMenuList(requestMap);
            }
        });

        tasks.add(new Callable<Object>(){//
            @Override
            public Object call() throws Exception {
                UserTaskService userTaskService = new UserTaskService();
                return userTaskService.getUserList(requestMap);
            }
        });
        //调用执行器执行
        List<Object> resultList = ThreadPoolUtil.executeTasks(tasks);
        //定义一个map来存放所有线程返回的结果，必须规定每个线程的返回值都是ResultVo，并且里面的值是map，这样才能追加起来最终形成一个map
        //为何这样做？因为map是键值对的形式，我们可以根据我们定义好的键值去获取具体的值，使用ResultVo还能具体知道线程里出现的异常和错误
        Map<String, Object> map = new HashMap<String, Object>();
        //循环获取结果
        for(Object obj : resultList){
            ResultVo resultVo = (ResultVo) obj;
            if(!resultVo.isSuccess()){
                System.out.println(resultVo.getMessage());
            }else{
                //把所有结果都汇集到一个map中，最终再根据这个map来取值满足我们后续的需要
                map.putAll((Map<String, Object>)resultVo.getValue());
            }
        }
        System.out.println(JacksonUtils.toJSONString(map));
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));
    }

}
