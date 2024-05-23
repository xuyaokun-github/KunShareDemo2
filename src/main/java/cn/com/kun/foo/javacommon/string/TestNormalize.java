package cn.com.kun.foo.javacommon.string;


public class TestNormalize {

    public static void main(String[] args) {

        //
        int index = "key_kkkk".lastIndexOf("_");
        System.out.println("key_kkkk".substring(0, index));

        System.out.println("𠮷".length());//2

        String str1 = "abc";
        String str2 = new String("abc");
        String str3 = str2.intern();
        System.out.println(str1 == str2);
        System.out.println(str2 == str3);
        System.out.println(str1 == str3);
    }
}
