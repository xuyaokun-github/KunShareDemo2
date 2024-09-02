package cn.com.kun.springframework.core.jackson.jacksonModuleParameterNamesDemo;

public class JmpnDemoVO {

    private String name;

    public JmpnDemoVO(String name) {
        this.name = name;
    }

//    @JsonCreator
//    public JmpnDemoVO(@JsonProperty("name") String name) {
//        this.name = name;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JmpnDemoVO{" +
                "name='" + name + '\'' +
                '}';
    }
}
