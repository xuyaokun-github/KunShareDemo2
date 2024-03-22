package cn.com.kun.framework.apache.skywalking.extend.blacktechnology;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

@Component
public class DispatcherServletAccessor implements InitializingBean {

    private static DispatcherServletAccessor dispatcherServletAccessor = null;

    //单例模式
    public static DispatcherServletAccessor getDispatcherServletAccessor() {
        return dispatcherServletAccessor;
    }

    @Autowired(required = false)
    private DispatcherServlet dispatcherServlet;

    public DispatcherServlet getDispatcherServlet(){

        return dispatcherServlet;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dispatcherServletAccessor = this;
    }

}
