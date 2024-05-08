package cn.com.kun.springframework.core.aop.dynamicAop;

public class ProxyMetaDefinitionChangeEvent {

    private int operateEventEnum;

    private ProxyMetaDefinition proxyMetaDefinition;

    public ProxyMetaDefinitionChangeEvent(int operateEventEnum, ProxyMetaDefinition proxyMetaDefinition) {
        this.operateEventEnum = operateEventEnum;
        this.proxyMetaDefinition = proxyMetaDefinition;
    }

    public int getOperateEventEnum() {
        return operateEventEnum;
    }

    public ProxyMetaDefinition getProxyMetaDefinition() {
        return proxyMetaDefinition;
    }

    public void setOperateEventEnum(int operateEventEnum) {
        this.operateEventEnum = operateEventEnum;
    }

    public void setProxyMetaDefinition(ProxyMetaDefinition proxyMetaDefinition) {
        this.proxyMetaDefinition = proxyMetaDefinition;
    }
}
