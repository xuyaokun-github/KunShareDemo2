package cn.com.kun.foo.javacommon.datatype;

public class TestInt {

    public static void main(String[] args) {

        /*
            2383946303
            2383975259017
            2383975258954
            30785206522672
         */
//        long days = 2383975259017L / (24 * 3600 * 1000) ;
//        System.out.println(days);//24天
        long years = 9223370318517714L / (365 * 24 * 3600 * 1000) ;
        System.out.println(years);//年

    }
}
