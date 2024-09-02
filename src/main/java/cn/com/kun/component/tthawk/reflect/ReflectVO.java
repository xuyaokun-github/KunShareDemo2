package cn.com.kun.component.tthawk.reflect;

import java.util.Map;

public class ReflectVO {

    private String className;

    private String method;

    private int methodParamSize;

    private Map<String, String> paramClassMap;

    private Map<String, String> methodClassMap;

    private Map<String, String> nestedExceptionClassMap;

    /**
     * 假如用了jsonParamClassMap，就不要用paramClassMap
     * paramClassMap会优先.
     *
     * 假如方法参数是int类型，可以使用包装类型java.lang.Integer作为反序列化的接收对象(最终传入方法执行时会自动拆箱)
     */
    private Map<String, String> jsonParamClassMap;

    /**
     * 支持基本类型、包装类型、普通Java实体类类型
     */
    private Map<String, String> jsonParamValueMap;

    /**
     * reflect
     * spring
     */
    private String acquireBeanMode = "reflect";

    private String springBeanName;

    private String jsonUtilsClassName = "";

    private String jsonUtilsMethodName = "";

    /////////////////////////////////////////////

    private Object springBean;

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

    public String getAcquireBeanMode() {
        return acquireBeanMode;
    }

    public void setAcquireBeanMode(String acquireBeanMode) {
        this.acquireBeanMode = acquireBeanMode;
    }

    public String getSpringBeanName() {
        return springBeanName;
    }

    public void setSpringBeanName(String springBeanName) {
        this.springBeanName = springBeanName;
    }

    public Object getSpringBean() {
        return springBean;
    }

    public void setSpringBean(Object springBean) {
        this.springBean = springBean;
    }

    public Map<String, String> getJsonParamClassMap() {
        return jsonParamClassMap;
    }

    public void setJsonParamClassMap(Map<String, String> jsonParamClassMap) {
        this.jsonParamClassMap = jsonParamClassMap;
    }

    public Map<String, String> getJsonParamValueMap() {
        return jsonParamValueMap;
    }

    public void setJsonParamValueMap(Map<String, String> jsonParamValueMap) {
        this.jsonParamValueMap = jsonParamValueMap;
    }

    public String getJsonUtilsClassName() {
        return jsonUtilsClassName;
    }

    public void setJsonUtilsClassName(String jsonUtilsClassName) {
        this.jsonUtilsClassName = jsonUtilsClassName;
    }

    public String getJsonUtilsMethodName() {
        return jsonUtilsMethodName;
    }

    public void setJsonUtilsMethodName(String jsonUtilsMethodName) {
        this.jsonUtilsMethodName = jsonUtilsMethodName;
    }
}
