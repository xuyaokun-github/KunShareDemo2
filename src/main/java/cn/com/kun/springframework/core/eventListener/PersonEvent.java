package cn.com.kun.springframework.core.eventListener;

public class PersonEvent {

    private PersonEventVo person;
    private String addOrUpdate;

    public PersonEvent(PersonEventVo person, String addOrUpdate) {
        this.person = person;
        this.addOrUpdate = addOrUpdate;
    }

    public PersonEventVo getPerson() {
        return person;
    }

    public void setPerson(PersonEventVo person) {
        this.person = person;
    }

    public String getAddOrUpdate() {
        return addOrUpdate;
    }

    public void setAddOrUpdate(String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }
}
