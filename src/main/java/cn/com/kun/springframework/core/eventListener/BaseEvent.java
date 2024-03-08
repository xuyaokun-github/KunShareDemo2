package cn.com.kun.springframework.core.eventListener;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * 通用的时间机制基类
 *
 * author:xuyaokun_kzx
 * date:2024/3/8
 * desc:
*/
public class BaseEvent<T> implements ResolvableTypeProvider {

    /**
     * 业务对象（不同的事件对应不同的VO）
     */
    private T data;

    /**
     * 这个属性是多余的
     */
    private String addOrUpdate;

    public BaseEvent(T data, String addOrUpdate) {
        this.data = data;
        this.addOrUpdate = addOrUpdate;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getAddOrUpdate() {
        return addOrUpdate;
    }

    public void setAddOrUpdate(String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }

    @Override
    public ResolvableType getResolvableType() {

        //解决泛型擦除问题
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getData()));
    }
}
