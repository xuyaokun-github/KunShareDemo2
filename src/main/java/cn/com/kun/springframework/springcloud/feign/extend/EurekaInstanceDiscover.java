package cn.com.kun.springframework.springcloud.feign.extend;//package com.xxl.job.admin.extend.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于Eureka实现的微服务实例发现器
 * version 1.1（适用于springboot2.7.X版本）
 *
 * springboot2.7版本已经不推荐ILoadBalancer
 *
 * author:xuyaokun_kzx
 * date:2023/11/3
 * desc:
*/
@ConditionalOnProperty(prefix = "eureka.client", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Component
public class EurekaInstanceDiscover {

    @Autowired
    private EurekaDiscoveryClient eurekaDiscoveryClient;

    /**
     *
     * @param clientName 微服务名
     * @return
     */
    public List<String> findAllHostnames(String clientName){

        List<String> hostnames = new ArrayList<>();
        List<EurekaServiceInstance> eurekaServiceInstanceList = findEurekaServiceInstances(clientName);
        if (!CollectionUtils.isEmpty(eurekaServiceInstanceList)){
            for (EurekaServiceInstance eurekaServiceInstance : eurekaServiceInstanceList){
                hostnames.add(eurekaServiceInstance.getInstanceInfo().getHostName());
            }
        }

        return hostnames;
    }

    /**
     * 获取全部IP 微服务名
     *
     * @param clientName
     * @return
     */
    public List<String> findAllIPAddrs(String clientName){

        List<String> hostPorts = new ArrayList<>();
        List<EurekaServiceInstance> eurekaServiceInstanceList = findEurekaServiceInstances(clientName);
        if (!CollectionUtils.isEmpty(eurekaServiceInstanceList)){
            for (EurekaServiceInstance eurekaServiceInstance : eurekaServiceInstanceList){
                hostPorts.add(eurekaServiceInstance.getInstanceInfo().getIPAddr());
            }
        }
//        Object object = loadBalancerClientFactory.getInstance(clientName);
//        if (object != null){
//            if (object instanceof RoundRobinLoadBalancer){
//                RoundRobinLoadBalancer roundRobinLoadBalancer = (RoundRobinLoadBalancer) object;
////                roundRobinLoadBalancer.choose();
//            }
//        }

        return hostPorts;
    }

    private List<EurekaServiceInstance> findEurekaServiceInstances(String clientName) {

        Assert.notNull(eurekaDiscoveryClient, "eurekaDiscoveryClient不能为空");
        List<EurekaServiceInstance> eurekaServiceInstanceList = null;
        List<ServiceInstance> serviceInstanceList = eurekaDiscoveryClient.getInstances(clientName);
        if (!CollectionUtils.isEmpty(serviceInstanceList)){
            for (ServiceInstance serviceInstance : serviceInstanceList){
                if (serviceInstance instanceof EurekaServiceInstance){
                    if (eurekaServiceInstanceList == null){
                        eurekaServiceInstanceList = new ArrayList<>();
                    }
                    EurekaServiceInstance eurekaServiceInstance = (EurekaServiceInstance) serviceInstance;
                    eurekaServiceInstanceList.add(eurekaServiceInstance);
                }
            }
        }
        return eurekaServiceInstanceList;
    }

    /**
     * 获取所有微服务名
     *
     * @return
     */
    public List<String> getServices(){

        List<String> services = eurekaDiscoveryClient.getServices();
        return services;
    }


}
