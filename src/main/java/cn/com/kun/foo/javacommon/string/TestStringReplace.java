package cn.com.kun.foo.javacommon.string;

public class TestStringReplace {

    public static void main(String[] args) {

        String str2 = "kungh\\suddddd\\ddddddk\\kkkk\\kkkk";
        System.out.println(str2.replace("\\", "\\\\"));

        /**
         * 需要将k替换成  \"
         */
        String str = "kunghsu";
        System.out.println(str.replace("k", "\\\""));
        System.out.println(str.replaceAll("k", "\\\\\""));

    }
}
