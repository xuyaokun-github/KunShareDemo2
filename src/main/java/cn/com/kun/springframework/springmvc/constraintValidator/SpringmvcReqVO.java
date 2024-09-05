package cn.com.kun.springframework.springmvc.constraintValidator;

import cn.com.kun.component.validationx.annotation.NotNullExtendField;
import cn.com.kun.component.validationx.annotation.NotNullExtendValidate;
import cn.com.kun.component.validationx.group.CRUDGroups;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Size;
import java.util.Date;

@NotNullExtendValidate(groups = {CRUDGroups.Create.class, CRUDGroups.Update.class})
public class SpringmvcReqVO {

    private Long id;//主键，将用雪花算法生成

    @Size(min = 4)
    private String idCard;//身份证号

    private String studentName;

//    @NotNullExtendField(condition = "idCard != null && idCard.equals(\"1000\")", columnName = "地址")
    @NotNullExtendField(condition = "idCard != null && idCard.equals(\"1000\")")
    private String address;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
