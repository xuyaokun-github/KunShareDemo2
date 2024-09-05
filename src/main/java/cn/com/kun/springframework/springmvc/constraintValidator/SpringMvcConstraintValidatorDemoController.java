package cn.com.kun.springframework.springmvc.constraintValidator;


import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.validationx.group.CRUDGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/spring-mvc-constraint-validator")
public class SpringMvcConstraintValidatorDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringMvcConstraintValidatorDemoController.class);

    @PostMapping("/test")
    public ResultVo test(@RequestBody @Validated SpringmvcReqVO springmvcReqVO){

        LOGGER.info("验证通过");
        return ResultVo.valueOfSuccess();
    }

    @PostMapping("/test-query")
    public ResultVo testQuery(@RequestBody @Validated({CRUDGroups.Read.class}) SpringmvcReqVO springmvcReqVO){

        LOGGER.info("验证通过");
        return ResultVo.valueOfSuccess();
    }

    @PostMapping("/test-save")
    public ResultVo testSave(@RequestBody @Validated({CRUDGroups.Create.class}) SpringmvcReqVO springmvcReqVO){

        LOGGER.info("验证通过");
        return ResultVo.valueOfSuccess();
    }


}
