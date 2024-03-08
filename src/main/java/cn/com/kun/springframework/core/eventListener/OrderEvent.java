package cn.com.kun.springframework.core.eventListener;

public class OrderEvent {

    private OrderEventVo person;
    private String addOrUpdate;

    public OrderEvent(OrderEventVo person, String addOrUpdate) {
        this.person = person;
        this.addOrUpdate = addOrUpdate;
    }

    public OrderEventVo getPerson() {
        return person;
    }

    public void setPerson(OrderEventVo person) {
        this.person = person;
    }

    public String getAddOrUpdate() {
        return addOrUpdate;
    }

    public void setAddOrUpdate(String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }
}
