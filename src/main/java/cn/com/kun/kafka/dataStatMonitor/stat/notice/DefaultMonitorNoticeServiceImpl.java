package cn.com.kun.kafka.dataStatMonitor.stat.notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultMonitorNoticeServiceImpl implements MonitorNoticeService{

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultMonitorNoticeServiceImpl.class);

    @Override
    public void notice(String result) {

        LOGGER.info("监控结果：{}", result);
    }

}
