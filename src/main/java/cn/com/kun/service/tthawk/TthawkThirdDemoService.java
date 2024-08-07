package cn.com.kun.service.tthawk;

import cn.com.kun.service.switchcheck.SwitchCheckerDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TthawkThirdDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SwitchCheckerDemoService.class);

    public void doWork5() {

        LOGGER.info("This is TthawkThirdDemoService doWork55555555.");
    }

    public void doWork6() {

        LOGGER.info("This is TthawkThirdDemoService doWork66666666.");
    }
}
