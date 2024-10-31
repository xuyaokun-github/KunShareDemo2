package cn.com.kun.service.tthawk;

import cn.com.kun.service.tthawk.vo.TthawkDemoEnum;
import cn.com.kun.service.tthawk.vo.TthawkDemoVO3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TthawkThirdDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkThirdDemoService.class);

    public void doWork5() {

        LOGGER.info("This is TthawkThirdDemoService doWork55555555.");
    }

    public void doWork6() /*throws InterruptedException*/{

        LOGGER.info("This is TthawkThirdDemoService doWork66666666.");
    }

    public void doWork7() /*throws InterruptedException*/{

        LOGGER.info("This is TthawkThirdDemoService doWork7. this:{}", this.toString());
    }

    /**
     * 枚举逻辑
     *
     */
    public void doWorkByEnum(TthawkDemoEnum tthawkDemoEnum, String name) {

        LOGGER.info("This is TthawkThirdDemoService doWorkByEnum.code:{} msg:{}", tthawkDemoEnum.getCode(), tthawkDemoEnum.getMsg());
    }

    /**
     * {
     *     "className":"cn.com.kun.service.tthawk.TthawkThirdDemoService",
     *     "method":"doWorkByInt",
     *     "methodParamSize":2,
     *     "methodClassMap":{
     *         "1":"int",
     *         "2":"java.lang.String"
     *     },
     *     "jsonParamClassMap":{
     *         "1":"java.lang.Integer",
     *         "2":"java.lang.String"
     *     },
     *     "jsonParamValueMap":{
     *         "1":"MTA=",
     *         "2":"IkZBSUxFRCI="
     *     },
     *     "jsonUtilsClassName":"cn.com.kun.common.utils.JacksonUtils",
     *     "jsonUtilsMethodName":"toJavaObject"
     * }
     *
     * @param number
     * @param name
     */
    public void doWorkByInt(int number, String name) {

        if (number == 10){
            LOGGER.info("This is TthawkThirdDemoService doWorkByInt.number:{} name:{}", number, name);
        }
    }

    public void doWorkByInteger(Integer number, String name) {

        if (number == 10){
            LOGGER.info("This is TthawkThirdDemoService doWorkByInteger.number:{} name:{}", number, name);
        }
    }


    public void doWorkByNoVoidConstruct(TthawkDemoVO3 tthawkDemoVO3) {

        if (tthawkDemoVO3 != null){
            LOGGER.info("This is TthawkThirdDemoService doWorkByNoVoidConstruct  {}", tthawkDemoVO3.getName());
        }
    }
}
