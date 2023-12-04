package cn.com.kun.springframework.springcloud.feign.vo;

import java.util.Date;

public class FeignJacksonDO {

    private String uuid;

    //通常Dao层不会加格式化注解，也不应该加
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
