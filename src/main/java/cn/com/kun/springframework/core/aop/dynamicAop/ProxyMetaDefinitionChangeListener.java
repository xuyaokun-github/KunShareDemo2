package cn.com.kun.springframework.core.aop.dynamicAop;


import org.springframework.context.event.EventListener;

public class ProxyMetaDefinitionChangeListener {

    private final AopPluginFactory aopPluginFactory;

    public ProxyMetaDefinitionChangeListener(AopPluginFactory aopPluginFactory) {
        this.aopPluginFactory = aopPluginFactory;
    }

    @EventListener
    public void listener(ProxyMetaDefinitionChangeEvent proxyMetaDefinitionChangeEvent){
        ProxyMetaInfo proxyMetaInfo = aopPluginFactory.getProxyMetaInfo(proxyMetaDefinitionChangeEvent.getProxyMetaDefinition());
        switch (proxyMetaDefinitionChangeEvent.getOperateEventEnum()){
            case 1:
                aopPluginFactory.installPlugin(proxyMetaInfo);
                break;
            case 2:
                aopPluginFactory.uninstallPlugin(proxyMetaInfo.getId());
                break;
        }

    }
}