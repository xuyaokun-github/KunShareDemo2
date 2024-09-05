package cn.com.kun.springframework.springmvc.constraintValidator;

import cn.com.kun.bean.model.StudentReqVO;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 多字段联动校验
 *
 * author:xuyaokun_kzx
 * date:2024/9/2
 * desc:
*/
public class StudentValidator implements ConstraintValidator<StudentValidate, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // 不为null才进行校验        
        if (value == null) {
            return false;
        }

        if (value instanceof StudentReqVO){
            StudentReqVO reqVO = (StudentReqVO) value;
            if (reqVO.getIdCard().startsWith("135")){
                //假如是135开头，才需要校验地址
                if (StringUtils.isNotBlank(reqVO.getAddress())){
                    return true;
                }else {
                    return false;
                }
            }else {
                return true;
            }
        }

        return true;
    }


}
