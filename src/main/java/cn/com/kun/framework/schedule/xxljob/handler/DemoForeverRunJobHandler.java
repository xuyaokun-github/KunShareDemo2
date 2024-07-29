package cn.com.kun.framework.schedule.xxljob.handler;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 样例
 *
 * @author xuyaokun
 * @date 2019/3/27 11:35
 */
@JobHandler(value="DemoForeverRunJobHandler")
@Component
public class DemoForeverRunJobHandler extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(DemoForeverRunJobHandler.class);

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        //使用XxlJobLogger.log输出的日志内容可以控制台端查看
        //但是不能输出到普通的console和file
        XxlJobLogger.log("XXL-JOB, Hello World. 我是定时任务DemoForeverRunJobHandler。");
        logger.info("XXL-JOB, Hello World. 我是定时任务DemoForeverRunJobHandler。");

        //模拟具体的业务逻辑
        AtomicBoolean flag = new AtomicBoolean(true);
        while (flag.get()){
            logger.info("beat at:" + System.currentTimeMillis());
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (Exception e){
                //故意不响应中断异常
            }
        }

        return SUCCESS;
    }

}
