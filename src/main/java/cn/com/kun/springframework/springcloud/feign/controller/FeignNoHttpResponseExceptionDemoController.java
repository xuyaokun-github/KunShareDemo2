package cn.com.kun.springframework.springcloud.feign.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.feign.client.KunwebdemoFeign;
import org.apache.http.NoHttpResponseException;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

@RequestMapping("/feign-NoHttpResponseException")
@RestController
public class FeignNoHttpResponseExceptionDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignNoHttpResponseExceptionDemoController.class);

    @Autowired
    private KunwebdemoFeign kunwebdemoFeign;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired //临时注释
    private PoolingHttpClientConnectionManager connectionManager;

    @PostConstruct
    public void init(){
        //为了更容易观察归还连接的过程，可以先将连接校验的过程设置久一点
        /*
            为了方便在单线程环境下复现NoHttpResponseException，
            ValidateAfterInactivity(默认是2000)尽量设置大一点，至少大于服务端的keep-alive
         */
        connectionManager.setValidateAfterInactivity(1000 * 2000);

    }

    @GetMapping("/test")
    public ResultVo test(){

        //请求第三方系统
        ResultVo resultVo = kunwebdemoFeign.result();
        return resultVo;
    }


    /**
     * 百分百几率复现NoHttpResponseException异常
     * Feign版
     *
     * @return
     */
    @GetMapping("/testInvokeForever")
    public ResultVo testInvokeForever(){

        new Thread(()->{

            int count = 0;
            while (true){
                //请求第三方系统
                ResultVo resultVo = kunwebdemoFeign.result();
                LOGGER.info("第{}次 resultVo:{}", ++count, JacksonUtils.toJSONString(resultVo));
                try {
//                    Thread.sleep(3000);
//                    Thread.sleep(4000);//当服务端设置为3000时
                    Thread.sleep(1910);//当服务端设置为1900时

//                    Thread.sleep(400);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return ResultVo.valueOfSuccess();
    }

    /**
     * 百分百几率复现NoHttpResponseException异常
     * Feign版
     *
     * @return
     */
    @GetMapping("/testInvokeForeverForRestTemplate")
    public ResultVo testInvokeForeverForRestTemplate(){

        new Thread(()->{

            int count = 0;
            while (true){
                //请求第三方系统
//                ResultVo resultVo = restTemplate.getForObject("http://127.0.0.1:8091/kunwebdemo/feigndemo/test", ResultVo.class);
                ResultVo resultVo = restTemplate.postForObject("http://127.0.0.1:8091/kunwebdemo/feigndemo/test-by-post", ResultVo.valueOfSuccess(), ResultVo.class);
                LOGGER.info("第{}次 resultVo:{}", ++count, JacksonUtils.toJSONString(resultVo));
                try {
//                    Thread.sleep(3000);
                    Thread.sleep(400);
//                    Thread.sleep(600);//当服务端设置为300时
//                    Thread.sleep(400);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return ResultVo.valueOfSuccess();
    }



    /**
     * 同一个线程内，多次请求
     * @return
     */
    @GetMapping("/test2")
    public ResultVo test2(){

        //请求第三方系统
        ResultVo resultVo = null;
        resultVo = kunwebdemoFeign.result();
        resultVo = kunwebdemoFeign.result();
        resultVo = kunwebdemoFeign.result();
        LOGGER.info("执行完毕");
        ResultVo res = ResultVo.valueOfSuccess();
        return resultVo;
    }

    /**
     * 当服务端的keep alive设置为1秒，本方法能100%复现NoHTTPResponse异常
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/test3")
    public ResultVo test3() throws InterruptedException {

        new Thread(()->{
            //请求第三方系统
            ResultVo resultVo = null;
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(1000);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(1000);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            LOGGER.info("执行完毕");
        }).start();


        ResultVo res = ResultVo.valueOfSuccess();
        return res;
    }

    @GetMapping("/test4")
    public ResultVo test4() throws InterruptedException {

        new Thread(()->{
            //请求第三方系统
            ResultVo resultVo = null;
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(2500);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(2500);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            LOGGER.info("执行完毕");
        }).start();


        ResultVo res = ResultVo.valueOfSuccess();
        return res;
    }

    /**
     * 验证服务端的连接是否会刷新keep alive时间
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/test5")
    public ResultVo test5() throws InterruptedException {

        new Thread(()->{
            //请求第三方系统
            ResultVo resultVo = null;
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(600);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(600);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(600);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(600);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(600);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(600);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(600);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            LOGGER.info("执行完毕");
        }).start();


        ResultVo res = ResultVo.valueOfSuccess();
        return res;
    }

    @GetMapping("/testAsync")
    public ResultVo testAsync(){

        AtomicLong atomicLong = new AtomicLong(0);
        //请求第三方系统
        for (int j = 0; j < 30; j++) {
            new Thread(()->{
                try {
                    for (int i = 0; i < 1000; i++) {
                        ResultVo resultVo = kunwebdemoFeign.result();
//                        LOGGER.info("res：{}", JacksonUtils.toJSONString(resultVo));
//                        try {
//                            Thread.sleep(70000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        atomicLong.incrementAndGet();
                    }
                    LOGGER.info("总数：{}", atomicLong.get());
                }catch (Exception e){
//                    e.printStackTrace();
                    if (e instanceof NoHttpResponseException){
                        e.printStackTrace();
                        LOGGER.error("主动关闭进程");
                        System.exit(0);
                    }else {
                        LOGGER.error("访问异常", e);
                    }
                }
            }).start();
        }
        
        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/testAsync101Thread")
    public ResultVo testAsync101Thread(){

        AtomicLong atomicLong = new AtomicLong(0);
        //请求第三方系统
        for (int j = 0; j < 105; j++) {
            new Thread(()->{
                try {
                    for (int i = 0; i < 2; i++) {
                        ResultVo resultVo = kunwebdemoFeign.result();
//                        LOGGER.info("res：{}", JacksonUtils.toJSONString(resultVo));
//                        try {
//                            Thread.sleep(70000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        atomicLong.incrementAndGet();
                    }
                    LOGGER.info("总数：{}", atomicLong.get());
                }catch (Exception e){
//                    e.printStackTrace();
                    if (e instanceof NoHttpResponseException){
                        e.printStackTrace();
                        LOGGER.error("主动关闭进程");
                        System.exit(0);
                    }else {
                        LOGGER.error("访问异常", e);
                    }
                }
            }).start();
        }

        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/testAsyncSameConnMoreTimes")
    public ResultVo testAsyncSameConnMoreTimes(){

        AtomicLong atomicLong = new AtomicLong(0);
        //请求第三方系统
        for (int j = 0; j < 1; j++) {
            new Thread(()->{
                try {
                    for (int i = 0; i < 200; i++) {
                        ResultVo resultVo = kunwebdemoFeign.result();
//                        LOGGER.info("res：{}", JacksonUtils.toJSONString(resultVo));
//                        try {
//                            Thread.sleep(70000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        atomicLong.incrementAndGet();
                    }
                    LOGGER.info("总数：{}", atomicLong.get());
                }catch (Exception e){
//                    e.printStackTrace();
                    if (e instanceof NoHttpResponseException){
                        e.printStackTrace();
                        LOGGER.error("主动关闭进程");
                        System.exit(0);
                    }else {
                        LOGGER.error("访问异常", e);
                    }
                }
            }).start();
        }

        return ResultVo.valueOfSuccess();
    }
}
