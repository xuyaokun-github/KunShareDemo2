package cn.com.kun.service.tthawk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TthawkFourthDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkFourthDemoService.class);

    public boolean doWork() {

        LOGGER.info("This is TthawkFourthDemoService.");
        return true;
    }


}
