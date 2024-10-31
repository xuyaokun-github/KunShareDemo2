package cn.com.kun.component.tthawk.dynamicaspect;

import cn.com.kun.component.tthawk.core.AopTargetUtil;
import cn.com.kun.component.tthawk.core.MethodKeyExceptionHolder;
import cn.com.kun.component.tthawk.core.TthawkSpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 鹰-动态切面
 *
 * author:xuyaokun_kzx
 * date:2024/10/31 19:24
 * desc:
*/
@ConditionalOnProperty(prefix = "tthawk", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@RequestMapping("/tthawk-da")
@RestController
public class TthawkDynamicAspectController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TthawkDynamicAspectController.class);

    @Autowired
    private DynamicAspectProcessor dynamicAspectProcessor;

    /**
     * 无法生效
     *
     * @return
     * @throws IOException
     */
    @GetMapping("/register-aspect")
    public String registerAspect() throws IOException {

        //动态插桩，往容器加入切面
        dynamicAspectProcessor.registerAspect("tthawkDynamicAspect");
        return "tthawk register aspect ok";
    }

    /**
     * 攻击前戏
     * 开启TthawkDynamicAspectPointcutAdvisor
     *
     * @return
     * @throws IOException
     */
    @GetMapping("/pre-attack")
    public String registerAdvisor() throws IOException {

        //动态插桩，往容器加入切面
        dynamicAspectProcessor.registerAdvisor("tthawkDynamicAspectPointcutAdvisor", TthawkDynamicAspectPointcutAdvisor.class);
        return "tthawk register aspect ok";
    }

    @GetMapping("/unregister-aspect")
    public String unregisterAspect() throws IOException {

        //动态插桩
        dynamicAspectProcessor.unregisterAspect("tthawkDynamicAspect");
        return "tthawk unregister aspect ok";
    }

    @PostMapping("/rebuild-bean")
    public String rebuildBean(@RequestBody DynamicAspectVO aspectVO) throws IOException {

        TthawkSpringBeanFactory.rebuildBean(aspectVO.getBeanName());
        return "tthawk ok";
    }

    @PostMapping("/attack")
    public String attack(@RequestBody DynamicAspectVO aspectVO) throws Exception {

        //1.自动填充classname
        if (aspectVO.getClassname() == null && aspectVO.getBeanName() != null){
            Object bean = TthawkSpringBeanFactory.getBean(aspectVO.getBeanName());
            if (bean != null){
                Object source = AopTargetUtil.getTarget(bean);
                if (source != null){
                    String classname = source.getClass().getName();
                    aspectVO.setClassname(classname);
                }
            }
        }

        //2.加入增强范围
        TthawkDynamicAspectClassMethodHolder.add(aspectVO.getClassname(), aspectVO.getMethodName());
        //3.重建bean,植入增强逻辑
        TthawkSpringBeanFactory.removeAdvisedBeans(aspectVO.getBeanName());
        TthawkSpringBeanFactory.rebuildBean(aspectVO.getBeanName());
        //4.正式attack
        String methodKey = aspectVO.getClassname() + "." + aspectVO.getMethodName();
        MethodKeyExceptionHolder.add(methodKey, aspectVO.getExceptionClass());

        return "tthawk dynamic aspect attack ok";
    }


    @PostMapping("/cancel-attack")
    public String cancelAttack(@RequestBody DynamicAspectVO aspectVO) throws Exception {

        //1.自动填充classname
        if (aspectVO.getClassname() == null && aspectVO.getBeanName() != null){
            Object bean = TthawkSpringBeanFactory.getBean(aspectVO.getBeanName());
            if (bean != null){
                Object source = AopTargetUtil.getTarget(bean);
                if (source != null){
                    String classname = source.getClass().getName();
                    aspectVO.setClassname(classname);
                }
            }
        }

        String methodKey = aspectVO.getClassname() + "." + aspectVO.getMethodName();
        MethodKeyExceptionHolder.remove(methodKey);
        return "tthawk dynamic aspect cancel attack ok";
    }

}
