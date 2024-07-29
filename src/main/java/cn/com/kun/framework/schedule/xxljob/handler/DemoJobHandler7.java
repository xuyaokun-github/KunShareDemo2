package cn.com.kun.framework.schedule.xxljob.handler;


import cn.com.kun.framework.schedule.xxljob.extend.ReturnTHelper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 样例
 *
 * @author xuyaokun
 * @date 2019/3/27 11:35
 */
@JobHandler(value="demoJobHandler7")
@Component
public class DemoJobHandler7 extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(DemoJobHandler7.class);

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        String uuid = UUID.randomUUID().toString();

        XxlJobLogger.log("XXL-JOB, Hello World.");
        logger.info("进入demoJobHandler，by @XxlJob!!");

        for (int i = 0; i < 1; i++) {
            /**
             * 注意，这个不会输出日志到控制台，它与log4j日志输出有区别（这个日志会输出到本地文件，最终展示到管理界面上）
             */
            XxlJobLogger.log(uuid + " beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }

        logger.info("业务执行结果:{}", uuid);
        return ReturnTHelper.buildSuccess("业务执行结果" + uuid);
    }

}
