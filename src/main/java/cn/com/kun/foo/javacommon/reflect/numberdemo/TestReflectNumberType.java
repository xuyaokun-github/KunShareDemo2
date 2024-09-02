package cn.com.kun.foo.javacommon.reflect.numberdemo;

public class TestReflectNumberType {

    public static void main(String[] args) {

        Class clazz = int.class;
        System.out.println(clazz.getName());
        Class<Integer> clazz2 = int.class;
        System.out.println(clazz2.getName());

        System.out.println(Integer.class.getName());
        try {
            clazz = Class.forName("java.lang.Integer");
            System.out.println(clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            //java.lang.ClassNotFoundException: int
            clazz = Class.forName("int");
            System.out.println(clazz.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
