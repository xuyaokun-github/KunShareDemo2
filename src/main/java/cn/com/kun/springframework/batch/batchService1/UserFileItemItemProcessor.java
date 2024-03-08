package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.batch.common.BatchDemoUtils;
import cn.com.kun.springframework.batch.common.BatchExecCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

/**
 * Created by xuyaokun On 2020/5/19 22:41
 * @desc: 
 */
public class UserFileItemItemProcessor implements ItemProcessor<UserFileItem, User> {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserFileItemItemProcessor.class);

    private StepExecution stepExecution;

    private String jobName;

    public UserFileItemItemProcessor(String jobName) {
        this.jobName = jobName;
    }

    public UserFileItemItemProcessor(StepExecution stepExecution, String jobName) {
        this.stepExecution = stepExecution;
        this.jobName = jobName;
    }

    @Override
    public User process(UserFileItem userFileItem) throws Exception {

        //处理行数加一
        String jobInstanceId = String.valueOf(stepExecution.getJobExecution().getJobInstance().getInstanceId());
        long currentCount = BatchExecCounter.countSuccess(jobInstanceId);

        //已读总数
        int readCount = stepExecution.getReadCount();

        //文件总行数
        long fileLineCount = stepExecution.getJobExecution().getExecutionContext().getLong("fileLineCount");
//        if (fileLineCount == readCount){//这种比较方法是不可靠的，不准确
//            LOGGER.info("处理到最后一行");
//        }else {
//            LOGGER.info("未到最后一行，当前readCount：{} 总行数：{}", readCount, fileLineCount);
//        }

        //
        BatchExecCounter.CountData countData = BatchExecCounter.getCountData(jobInstanceId);
        if (countData != null){
            if (fileLineCount == currentCount){
                LOGGER.info("处理到最后一行");
            }else {
                LOGGER.info("未到最后一行，当前readCount：{} 总行数：{}", currentCount, fileLineCount);
            }
        }

        //检查是否需要停止(测试并发问题时使用)
        //根据jobName来获取标志，假如识别到停止标志，就退出
//        if (SimpleStopHelper.isNeedStop(jobName)){
//            LOGGER.info("识别到停止标志，主动结束Job[{}]", jobName);
//            LOGGER.info("当前process阶段userFileItem：{}", userFileItem.getUid());
//            throw new RuntimeException("Job主动停止");
//        }

        //中间处理器
        /*
        接收的是来自读操作提供的UserFileItem，然后返回一个User给写操作去做具体的后续处理
        读操作主要负责读，数据来自哪里
        中间处理器重在一些信息的封装
        写操作主要负责保存数据，数据该落地保存在哪里
         */
//        logger.debug("进入处理器");
        int i = userFileItem.getType();
        User user  = new User();
        user.setFirstname("fisrt" + i);
        user.setLastname("kunghsu" + i);
        user.setEmail(i + "@qq.com");
        user.setPhone("135-" +System.currentTimeMillis());
        user.setCreateTime(new Date());
        //后面的写操作，可以使用一些spring自带提供的，注意这里假如返回的是null，spring不会对它做处理
        /*
        源码org.springframework.batch.core.step.item.SimpleChunkProcessor.transform，这里会对返回的对象进行判断，假如不空才会放入outputs中
         */
//        return null;

//        if(userFileItem.getUid() == 6){
//            return null;
//        }

//        if(userFileItem.getUid() == 11){
//            //模拟一个停止异常（主动停止任务）
//            throw new RuntimeException("任务主动停止");
//        }

//        if (i != 100){
//            return null;//验证死循环问题时使用
//        }

        LOGGER.info("中间操作, 行号：{} 行内容：{}", userFileItem.getLineNumber(), JacksonUtils.toJSONString(userFileItem));
//        BatchDemoUtils.addLineNumber(userFileItem.getLineNumber());
        BatchDemoUtils.addLineNumber(String.valueOf(currentCount));
        return user;
    }

}
