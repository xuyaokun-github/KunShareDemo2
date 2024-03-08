package cn.com.kun.springframework.core.eventListener;

public class OrderEventVo {

    private String name;

    public OrderEventVo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
