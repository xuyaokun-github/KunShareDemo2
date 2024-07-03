package cn.com.kun.service.hccounter;

import cn.com.kun.component.hccounter.CountUpdateFunction;
import cn.com.kun.component.hccounter.HighConcurrencyCounter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class HccounterDemoService {

    private HighConcurrencyCounter counter;

    private Map<String, AtomicLong> tempCounterMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        //使用建议，没必要把它定义成bean丢给spring，当然也可以这么做
        counter = new HighConcurrencyCounter("demo1");
        //注册实现
        counter.setCountUpdateFunction(new CountUpdateFunction() {
            @Override
            public void update(String countKey, long count) {
                updateValue(countKey, count);
            }

        });
        counter.setUpdateIntervalMs(2000);
        //必须调用start
//        counter.start();*
    }

    public void init(String uuid){
        tempCounterMap.put(uuid, new AtomicLong());
    }

    public void add(String uuid){

        counter.add(uuid, 1);
    }

    public void updateValue(String uuid, long count){

        tempCounterMap.get(uuid).addAndGet(count);
    }

    public long getTotal(String uuid){

        return tempCounterMap.get(uuid).get();
    }

}
