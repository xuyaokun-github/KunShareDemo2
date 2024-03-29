package cn.com.kun.springframework.core.eventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListenerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventListenerService.class);

    @EventListener
    public void handlePersonEvent(PersonEvent personEvent) {
        LOGGER.info("监听到PersonEvent: {}", personEvent);
    }

    @EventListener
    public void handleOrderEvent(OrderEvent orderEvent) {
        LOGGER.info("监听到OrderEvent: {}", orderEvent);
    }

    /**
     * 虽然能避免泛型擦除问题
     * 这样写有缺点，太多if else.
     * @param baseEvent
     */
//    @EventListener
//    public void handleBaseEvent(BaseEvent<?> baseEvent) {
//        Object data = baseEvent.getData();
//        if (data instanceof PersonEventVo){
//            LOGGER.info("监听到PersonEventVo: {}", data);
//        }else if (data instanceof OrderEventVo){
//            LOGGER.info("监听到OrderEventVo: {}", data);
//        }
//    }

    /**
     * 下面两个方法会有泛型擦除问题
     * 用 BaseEvent  implements ResolvableTypeProvider解决问题
     *
     * 最佳实践：一个事件就定义一个@EventListener，不用放在同一个类，
     * 假如放在同一个类，会注入太多Service
     * 在发布事件的时候用SpringContext工具类进行发布，就不用注入事件Service了
     *
     * @param personEvent
     */
    @EventListener
    public void handleBasePersonEvent(BaseEvent<PersonEventVo> personEvent) {
        LOGGER.info("监听到PersonEvent: {}", personEvent);
    }

    @EventListener
    public void handleBaseOrderEvent(BaseEvent<OrderEventVo> orderEvent) {
        LOGGER.info("监听到OrderEvent: {}", orderEvent);
    }
}
