package cn.com.kun.kafka.topicIsolation.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 主题定义
 *
 * author:xuyaokun_kzx
 * date:2023/1/6
 * desc:
*/
public class TopicBean {

    /**
     * 主题前缀
     */
    private String topicPrefix;

    /**
     * 业务列
     */
    private List<String> bizColumns;

    /**
     * 优先级
     * 高中低：
     * high
     * middle
     * low
     * 假如设置空，表示不应用优先级
     *
     */
    private String priority;

    /**
     * 主题名
     */
    private String topicName;

    public TopicBean(String topicPrefix, List<String> bizColumns, String priority) {
        this.topicPrefix = topicPrefix;
        this.bizColumns = bizColumns;
        this.priority = priority;
        this.topicName = buildTopicName();
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public List<String> getBizColumns() {
        return bizColumns;
    }

    public void setBizColumns(List<String> bizColumns) {
        this.bizColumns = bizColumns;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 返回构建好的主题名
     *
     * @return
     */
    public String buildTopicName(){

        /**
         * 假如没指定优先级，说明无需拼接优先级后缀，则主题名不含高中低
         */
        if (StringUtils.isEmpty(priority)){
            return topicPrefix + "_" + StringUtils.join(bizColumns, "_");
        }

        return topicPrefix + "_" + StringUtils.join(bizColumns, "_") + "_" + priority;
    }


    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
