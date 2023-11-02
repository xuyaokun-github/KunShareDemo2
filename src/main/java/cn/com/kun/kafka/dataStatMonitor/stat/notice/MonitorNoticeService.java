package cn.com.kun.kafka.dataStatMonitor.stat.notice;

public interface MonitorNoticeService {

    /**
     * 监控结果通知
     *
     * @param result
     */
    void notice(String result);

}
