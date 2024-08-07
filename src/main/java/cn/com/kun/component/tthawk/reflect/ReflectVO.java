package cn.com.kun.component.tthawk.reflect;

import java.util.Map;

public class ReflectVO {

    private String className;

    private String method;

    private int methodParamSize;

    private Map<String, String> paramClassMap;

    private Map<String, String> methodClassMap;

    private Map<String, String> nestedExceptionClassMap;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getMethodParamSize() {
        return methodParamSize;
    }

    public void setMethodParamSize(int methodParamSize) {
        this.methodParamSize = methodParamSize;
    }

    public Map<String, String> getParamClassMap() {
        return paramClassMap;
    }

    public void setParamClassMap(Map<String, String> paramClassMap) {
        this.paramClassMap = paramClassMap;
    }

    public Map<String, String> getMethodClassMap() {
        return methodClassMap;
    }

    public void setMethodClassMap(Map<String, String> methodClassMap) {
        this.methodClassMap = methodClassMap;
    }

    public Map<String, String> getNestedExceptionClassMap() {
        return nestedExceptionClassMap;
    }

    public void setNestedExceptionClassMap(Map<String, String> nestedExceptionClassMap) {
        this.nestedExceptionClassMap = nestedExceptionClassMap;
    }

}
