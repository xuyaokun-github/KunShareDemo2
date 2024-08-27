package cn.com.kun.service.tthawk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.net.SocketTimeoutException;

@Service
public class TthawkFirstDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkFourthDemoService.class);

    @Autowired
    private TthawkSecondDemoService tthawkSecondDemoService;

    @Autowired
    private TthawkThirdDemoService tthawkThirdDemoService;

    @Autowired
    private TthawkFourthDemoService tthawkFourthDemoService;

    /**
     * {
     *     "className":"cn.com.kun.service.tthawk.TthawkFirstDemoService",
     *     "method":"doWork",
     *     "methodParamSize":0,
     *     "acquireBeanMode":"spring"
     *
     * }
     */
    public void doWork() {

        LOGGER.info("This is TthawkFirstDemoService.");
        try {
            tthawkSecondDemoService.doWork();
            tthawkThirdDemoService.doWork5();
            boolean res = tthawkFourthDemoService.doWork();
            if (res){
                LOGGER.info("tthawkFourthDemoService执行成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void doWork2(TthawkDemoVO1 tthawkDemoVO1, TthawkDemoVO2 tthawkDemoVO2) {

        LOGGER.info("This is TthawkFirstDemoService.");
        try {
            tthawkSecondDemoService.doWork();
            tthawkThirdDemoService.doWork5();
            boolean res = tthawkFourthDemoService.doWork();
            if (res && tthawkDemoVO1.getName().equals("kunghsu")){
                LOGGER.info("tthawkFourthDemoService执行成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void doWork3(TthawkDemoVO1 tthawkDemoVO1, TthawkDemoVO2 tthawkDemoVO2) {

        LOGGER.info("This is TthawkFirstDemoService.");
        try {
            boolean res = tthawkFourthDemoService.doWork();
        }catch (Exception e){
            if (isHttpSocketTimeoutException(e)){
                LOGGER.info("出现超时异常");
            }
            e.printStackTrace();
        }


    }

    private boolean isHttpSocketTimeoutException(Exception e) {

        return e.getCause() != null && e.getCause() instanceof ResourceAccessException
                && e.getCause().getCause() != null && e.getCause().getCause() instanceof SocketTimeoutException;

    }


}
