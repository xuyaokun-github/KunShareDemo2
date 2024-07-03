package cn.com.kun.service.mybatis.extend.multidbswitch.enums;


public enum HolderTypeEnum {

    GLOBAL("Global"),
    THREAD("Thread");

    HolderTypeEnum(String holderType) {
        this.holderType = holderType;
    }

    public String holderType;

    public String getHolderType() {
        return holderType;
    }

    public void setHolderType(String holderType) {
        this.holderType = holderType;
    }
}
