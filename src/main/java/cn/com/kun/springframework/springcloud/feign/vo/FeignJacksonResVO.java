package cn.com.kun.springframework.springcloud.feign.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class FeignJacksonResVO {

    private String uuid;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
