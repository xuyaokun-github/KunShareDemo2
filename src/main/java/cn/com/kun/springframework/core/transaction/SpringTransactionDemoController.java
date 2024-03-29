package cn.com.kun.springframework.core.transaction;

import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/spring-transaction")
@RestController
public class SpringTransactionDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringTransactionDemoController.class);

    @Autowired
    SpringTransactionDemoService springTransactionDemoService;

    /**
     * 分析一个@JsonFormat失效的问题
     * @return
     */
    @GetMapping("/test")
    public ResultVo test() throws Exception {

        springTransactionDemoService.method1();
//        springTransactionDemoService.method2();
        return ResultVo.valueOfSuccess();
    }
}
