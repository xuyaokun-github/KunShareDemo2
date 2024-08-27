package cn.com.kun.component.tthawk.reflect;

public enum AcquireBeanMode {

    REFLECT("reflect", "REFLECT"),
    SPRING("spring", "SPRING");

    private String mode;
    private String desc;

    AcquireBeanMode(String mode, String desc) {
        this.mode = mode;
        this.desc = desc;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
