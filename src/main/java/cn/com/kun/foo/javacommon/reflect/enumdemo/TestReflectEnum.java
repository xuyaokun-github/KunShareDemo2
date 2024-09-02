package cn.com.kun.foo.javacommon.reflect.enumdemo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestReflectEnum {

    public static void main(String[] args) {

        findAllEnums();
        findEnumItemByEnumName("");

    }

    private static void findEnumItemByEnumName(String s) {

        // 获取枚举Class对象
        Class clazz = MyDemoEnum.class;
        // 使用反射获取valueOf方法
        Method valueOfMethod = null;
        try {
            valueOfMethod = clazz.getMethod("valueOf", String.class);
            // 反射调用valueOf方法创建枚举对象
            Object obj = valueOfMethod.invoke(null, "ERROR");
            System.out.println(valueOfMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    private static void findAllEnums() {

        // 获取枚举Class对象
        Class clazz = MyDemoEnum.class;
        Method valuesMethod = null;
        try {
            valuesMethod = clazz.getMethod("values");
            Object allEnumObj = valuesMethod.invoke(null);
            System.out.println(allEnumObj);
            if (allEnumObj instanceof MyDemoEnum[]){
                MyDemoEnum[] myDemoEnums = (MyDemoEnum[]) allEnumObj;
                //拿到第一个元素
                System.out.println(myDemoEnums[0]);
                System.out.println("end");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
