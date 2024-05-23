package cn.com.kun.config.hccounter;

import cn.com.kun.component.hccounter.CountUpdateFunction;
import cn.com.kun.component.hccounter.HighConcurrencyCounter;
import cn.com.kun.service.hccounter.HccounterDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 高并发增量计数器使用例子
 *
 * author:xuyaokun_kzx
 * date:2024/5/22
 * desc:
*/
@Configuration
public class HccounterConfig {

    /**
     * 假如HccounterDemoService也注入HighConcurrencyCounter，会有循环依赖问题
     */
    @Autowired
    HccounterDemoService hccounterDemoService;

//    @Bean //使用建议，没必要把它定义成bean丢给spring，当然也可以这么做
    public HighConcurrencyCounter highConcurrencyCounter(){

        HighConcurrencyCounter counter = new HighConcurrencyCounter("demo1");
        //注册实现
        counter.setCountUpdateFunction(new CountUpdateFunction() {
            @Override
            public void update(String countKey, long count) {
                hccounterDemoService.updateValue(countKey, count);
            }

        });
        return counter;
    }


}
