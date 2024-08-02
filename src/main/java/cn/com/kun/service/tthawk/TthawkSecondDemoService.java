package cn.com.kun.service.tthawk;

import cn.com.kun.service.switchcheck.SwitchCheckerDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TthawkSecondDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SwitchCheckerDemoService.class);

    public void doWork() {

        LOGGER.info("This is TthawkSecondDemoService.");
    }


    public void doWork1() {
        LOGGER.info("This is TthawkSecondDemoService doWork1111111.");
    }


    public void doWork2() {
        LOGGER.info("This is TthawkSecondDemoService doWork22222222.");
    }

    public void doWork3() {
        LOGGER.info("This is TthawkSecondDemoService doWork22222222.");
    }
}
