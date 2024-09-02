package cn.com.kun.service.tthawk.vo;

public class TthawkDemoVO3 {

    private String name;

//    public TthawkDemoVO3() {
//    }

//    public TthawkDemoVO3(String name) {
//        this.name = name;
//    }

//    public TthawkDemoVO3(String name, int age, long width) {
//        this.name = name;
//    }


    public TthawkDemoVO3(String name, String name2, TthawkDemoVO2 tthawkDemoVO2) {
        this.name = name;
    }


//    @JsonCreator
//    public TthawkDemoVO3(@JsonProperty("name") String name) {
//        this.name = name;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
