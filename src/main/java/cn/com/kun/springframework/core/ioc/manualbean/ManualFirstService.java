package cn.com.kun.springframework.core.ioc.manualbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 这是一个普通的spring bean
 *
 * author:xuyaokun_kzx
 * date:2024/10/29
 * desc:
*/
@Service
public class ManualFirstService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ManualFirstService.class);

    @ManualbeanTimeLog
    public void method1(){
        LOGGER.info("ManualFirstService method1");
    }

    public void method2(){
        LOGGER.info("ManualFirstService method2");
    }

    public void method3(){
        LOGGER.info("ManualFirstService method3");
    }
}
