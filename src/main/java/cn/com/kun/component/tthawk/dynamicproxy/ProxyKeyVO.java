package cn.com.kun.component.tthawk.dynamicproxy;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/8/1
 * desc:
*/
public class ProxyKeyVO {

    private String beanClassName;

    private String methodKey;

    private String exceptionClass;

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public void setMethodKey(String methodKey) {
        this.methodKey = methodKey;
    }
}
