package cn.com.kun.component.tthawk.dynamicaspect;

/**
 * author:xuyaokun_kzx
 * date:2024/10/31 19:24
 * desc:
*/
public class DynamicAspectVO {

    private String beanName;

    private String classname;

    private String methodName;

    private String exceptionClass;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }
}
