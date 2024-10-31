package cn.com.kun.springframework.core.ioc.manualbean;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.SpringContextUtil;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用不同类型的类，无法复现spring.main.allow-bean-definition-overriding报错。
 *
 * author:xuyaokun_kzx
 * date:2023/11/17
 * desc:
*/
@RequestMapping("/manualbean-demo")
@RestController
public class ManualbeanDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ManualbeanDemoController.class);

    @Autowired
    private SpringBeanFactory springBeanFactory;

    @Autowired
    private ManualFirstService manualFirstService;

    /**
     * 手动添加bean到容器
     *
     * @return
     */
    @GetMapping("/addManualDemoServiceBean")
    public ResultVo<User> addManualDemoServiceBean(){

        SpringBeanFactory.setBeanDefinition("manualDemoService", ManualDemoService.class);
        Object bean = SpringContextUtil.getBean("manualDemoService");
        return ResultVo.valueOfSuccess();
    }

    /**
     * 测试方法
     *
     * @return
     */
    @GetMapping("/testInvokeManualDemoService")
    public ResultVo<User> testInvokeManualDemoService(){

        //尝试获取该bean，spring就会开始原汁原味的创建bean过程
        Object bean = SpringContextUtil.getBean("manualDemoService");
        if (bean != null){
            ManualDemoService manualDemoService = (ManualDemoService) bean;
            manualDemoService.method();
        }

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/testInvokeManualFirstService")
    public ResultVo testInvokeManualFirstService(){

        manualFirstService.method1();

        LOGGER.info("第二次调用");

        Object bean = SpringContextUtil.getBean("manualFirstService");
        if (bean != null){
            ManualFirstService manualFirstService = (ManualFirstService) bean;
            manualFirstService.method1();
        }

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/manualFirstServiceMethod2")
    public ResultVo manualFirstServiceMethod2(){

        manualFirstService.method2();

        LOGGER.info("第二次调用");

        Object bean = SpringContextUtil.getBean("manualFirstService");
        if (bean != null){
            ManualFirstService manualFirstService = (ManualFirstService) bean;
            manualFirstService.method2();
        }

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/manualFirstServiceMethod3")
    public ResultVo manualFirstServiceMethod3(){

        manualFirstService.method3();

        LOGGER.info("第二次调用");

        Object bean = SpringContextUtil.getBean("manualFirstService");
        if (bean != null){
            ManualFirstService manualFirstService = (ManualFirstService) bean;
            manualFirstService.method3();
        }

        return ResultVo.valueOfSuccess();
    }

    /**
     * 这种方式手动提交切面，无法生效
     *
     * @return
     */
    @GetMapping("/addAspect")
    public ResultVo<User> addAspect(){

        SpringBeanFactory.setBeanDefinition("manualbeanDemoAspect", ManualbeanDemoAspect.class);

        //尝试获取一下bean
        Object bean = SpringContextUtil.getBean("manualbeanDemoAspect");

        SpringBeanFactory.refreshCachedAdvisorBeanNames();

        return ResultVo.valueOfSuccess();
    }

    /**
     * 动态添加切面，亲测成功
     *
     * @return
     */
    @GetMapping("/addAspect2")
    public ResultVo<User> addAspect2(){

        SpringBeanFactory.setBeanDefinition("manualbeanSecondPointcutAdvisor", ManualbeanSecondPointcutAdvisor.class);

        //尝试获取一下bean
        Object bean = SpringContextUtil.getBean("manualbeanSecondPointcutAdvisor");

        //假如希望后续创建bean发现刚加进去的增强器，需要刷新org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper.cachedAdvisorBeanNames
        //得用反射做这个事情
        SpringBeanFactory.refreshCachedAdvisorBeanNames();

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/add-manualbeanThirdPointcutAdvisor")
    public ResultVo<User> addManualbeanThirdPointcutAdvisor(){

        SpringBeanFactory.setBeanDefinition("manualbeanThirdPointcutAdvisor", ManualbeanThirdPointcutAdvisor.class);

        //尝试获取一下bean
        Object bean = SpringContextUtil.getBean("manualbeanThirdPointcutAdvisor");

        //假如希望后续创建bean发现刚加进去的增强器，需要刷新org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper.cachedAdvisorBeanNames
        //得用反射做这个事情
        SpringBeanFactory.refreshCachedAdvisorBeanNames();

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/refreshCachedAdvisorBeanNames")
    public ResultVo<User> refreshCachedAdvisorBeanNames(){

        SpringBeanFactory.refreshCachedAdvisorBeanNames();

        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/rebuildBean")
    public ResultVo<User> rebuildBean(){

        SpringBeanFactory.removeAdvisedBeans("manualFirstService");
        SpringBeanFactory.rebuildBean("manualFirstService");

        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/rebuildBean2")
    public ResultVo<User> rebuildBean2(){

        //重建org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#advisedBeans
        SpringBeanFactory.removeAdvisedBeans("manualFirstService");
        SpringBeanFactory.rebuildBean2("manualFirstService", ManualFirstService.class);

        return ResultVo.valueOfSuccess();
    }
}
