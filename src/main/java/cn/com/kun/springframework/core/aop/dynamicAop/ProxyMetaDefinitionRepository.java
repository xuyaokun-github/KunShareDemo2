package cn.com.kun.springframework.core.aop.dynamicAop;

import cn.com.kun.springframework.core.eventListener.SpringEventUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyMetaDefinitionRepository {

    private Map<String, ProxyMetaDefinition> proxyMetaDefinitionMap = new ConcurrentHashMap<>();

    public List<ProxyMetaDefinition> getProxyMetaDefinitions() {

        List<ProxyMetaDefinition> list = null;
        if (proxyMetaDefinitionMap.size() > 0){
            list = new ArrayList<>(proxyMetaDefinitionMap.values());
        }
        return list;
    }

    public ProxyMetaDefinition getProxyMetaDefinition(String proxyMetaDefinitionId) {

        return proxyMetaDefinitionMap.get(proxyMetaDefinitionId);
    }

    public void save(ProxyMetaDefinition definition) {
        proxyMetaDefinitionMap.put(definition.getId(), definition);
        //触发事件
        SpringEventUtil.publishEvent(new ProxyMetaDefinitionChangeEvent(1, definition));
    }

    public void delete(String proxyMetaDefinitionId) {
        proxyMetaDefinitionMap.remove(proxyMetaDefinitionId);
    }

}
