package cn.com.kun.springframework.core.eventListener;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class BaseEvent<T> implements ResolvableTypeProvider {

    private T data;
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
