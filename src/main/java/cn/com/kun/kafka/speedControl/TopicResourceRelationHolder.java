package cn.com.kun.kafka.speedControl;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/10/24
 * desc:
*/
@Component
public class TopicResourceRelationHolder {

    private Map<String, Set<String>> topicResourceRelationMap = new HashMap<>();

    public synchronized void put(String topic, String resourceName){

        if (!topicResourceRelationMap.containsKey(topic)){
            topicResourceRelationMap.put(topic, new HashSet<>());
        }
        topicResourceRelationMap.get(topic).add(resourceName);
    }


    public String[] get(String topic) {

        Set<String> resourceSet = topicResourceRelationMap.get(topic);
        int size = resourceSet != null ? resourceSet.size() : 0;
        if (size > 0){
            return resourceSet.toArray(new String[size]);
        }
        return null;
    }

}
