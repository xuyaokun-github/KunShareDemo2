package cn.com.kun.component.tthawk.dynamicpointcut;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/8/1
 * desc:
*/
public class MethodKeyVO {

    private String methodKey;

    private String exceptionClass;

    private String nestedExceptionClass;

    public String getMethodKey() {
        return methodKey;
    }

    public void setMethodKey(String methodKey) {
        this.methodKey = methodKey;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getNestedExceptionClass() {
        return nestedExceptionClass;
    }

    public void setNestedExceptionClass(String nestedExceptionClass) {
        this.nestedExceptionClass = nestedExceptionClass;
    }
}
