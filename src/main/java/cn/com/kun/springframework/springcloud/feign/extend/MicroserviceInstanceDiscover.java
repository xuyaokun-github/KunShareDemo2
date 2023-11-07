package cn.com.kun.springframework.springcloud.feign.extend;//package com.xxl.job.admin.extend.component;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于feign+Ribbon实现的微服务实例搜索器
 * version 1.1（适用于springboot2.7.X版本）
 *
 * springboot2.7版本已经不推荐ILoadBalancer
 *
 * author:xuyaokun_kzx
 * date:2023/11/3
 * desc:
*/
@Component
public class MicroserviceInstanceDiscover {

    @Autowired
    private LoadBalancerClientFactory loadBalancerClientFactory;

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

        Assert.notNull(loadBalancerClientFactory, "loadBalancerClientFactory不能为空");
        List<EurekaServiceInstance> eurekaServiceInstanceList = null;
        //获取ServiceInstanceListSupplier实例
        ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider = loadBalancerClientFactory.getLazyProvider(clientName, ServiceInstanceListSupplier.class);
        List<List<ServiceInstance>> lists = serviceInstanceListSupplierProvider.getIfAvailable().get().collectList().block();
        if (!CollectionUtils.isEmpty(lists)){
            for (List<ServiceInstance> list : lists){
                if (!CollectionUtils.isEmpty(list)){
                    for (ServiceInstance serviceInstance : list){
                        if (serviceInstance instanceof EurekaServiceInstance){
                            if (eurekaServiceInstanceList == null){
                                eurekaServiceInstanceList = new ArrayList<>();
                            }
                            EurekaServiceInstance eurekaServiceInstance = (EurekaServiceInstance) serviceInstance;
                            eurekaServiceInstanceList.add(eurekaServiceInstance);
                        }
                    }
                }
            }
        }
        return eurekaServiceInstanceList;
    }

}
