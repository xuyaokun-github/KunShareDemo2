package cn.com.kun.springframework.core.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SpringScheduleDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringScheduleDemoService.class);


//    @Scheduled(cron = "0/15 * * * * ?")
    public void work(){
        LOGGER.info("SpringScheduleDemoService working....");
    }

}
