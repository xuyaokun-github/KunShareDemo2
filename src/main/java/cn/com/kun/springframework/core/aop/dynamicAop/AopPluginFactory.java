package cn.com.kun.springframework.core.aop.dynamicAop;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.NoSuchElementException;

public class AopPluginFactory {

    private static final Object SPIILT = "|";
    private static final String PROXY_PLUGIN_PREFIX = "";

    private DefaultListableBeanFactory defaultListableBeanFactory;

    public ProxyMetaInfo getProxyMetaInfo(ProxyMetaDefinition proxyMetaDefinition) {

        return null;
    }

    public void installPlugin(ProxyMetaInfo proxyMetaInfo) {
        if(StringUtils.isEmpty(proxyMetaInfo.getId())){
            proxyMetaInfo.setId(proxyMetaInfo.getProxyUrl() + SPIILT + proxyMetaInfo.getProxyClassName());
        }
        AopUtil.registerProxy(defaultListableBeanFactory, proxyMetaInfo);
    }

    public void uninstallPlugin(String id){
        String beanName = PROXY_PLUGIN_PREFIX + id;
        if(defaultListableBeanFactory.containsBean(beanName)){
            AopUtil.destoryProxy(defaultListableBeanFactory,id);
        }else{
            throw new NoSuchElementException("Plugin not found: " + id);
        }
    }
}
