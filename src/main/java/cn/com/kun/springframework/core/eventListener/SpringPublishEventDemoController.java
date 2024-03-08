package cn.com.kun.springframework.core.eventListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/spring-event")
public class SpringPublishEventDemoController {


    @Resource
    private ApplicationContext applicationContext;

    @GetMapping("/publishEvent")
    public void publishEvent() {
        applicationContext.publishEvent(new PersonEvent(new PersonEventVo("why"), "add"));
        applicationContext.publishEvent(new OrderEvent(new OrderEventVo("why2"), "add2"));

    }

    @GetMapping("/publishEvent2")
    public void publishEvent2() {
        applicationContext.publishEvent(new BaseEvent<PersonEventVo>(new PersonEventVo("why"), "add"));
        applicationContext.publishEvent(new BaseEvent<OrderEventVo>(new OrderEventVo("why2"), "add2"));

    }
}
