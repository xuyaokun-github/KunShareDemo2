package cn.com.kun.kafka.common;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 位移提交回调器
 *
 * author:xuyaokun_kzx
 * date:2024/8/6
 * desc:
*/
public class CommonOffsetCommitCallback implements OffsetCommitCallback {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommonOffsetCommitCallback.class);

    @Override
    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {

        if (exception != null){
            if (isOffsetCommitTimeoutException(exception)){
                LOGGER.warn("位移提交超时[假如集群状态健康，可忽略该日志]", exception);
                return;
            }else {
                LOGGER.error("位移提交异常", exception);
            }
        }

    }

    private boolean isOffsetCommitTimeoutException(Exception exception) {

        boolean isTimeoutException = false;
        Throwable throwable = exception.getCause();
        if (throwable != null && throwable instanceof TimeoutException){
            isTimeoutException = true;
        }
        return isTimeoutException;
    }


}
