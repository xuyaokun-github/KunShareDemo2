package cn.com.kun.foo.javacommon.random;

import java.util.Random;

public class RandomTest {

    public static void main(String[] args) {

        randomString(-229985452);
        System.out.println("------------");
        randomString(-229985452);

        System.out.println("------------");
        randomString2(-229985452);//输出hello
        randomString2(-147909649);//输出world

    }

    private static void randomString(int i) {
        Random ran = new Random(i);
        System.out.println(ran.nextInt());
        System.out.println(ran.nextInt());
        System.out.println(ran.nextInt());
        System.out.println(ran.nextInt());
        System.out.println(ran.nextInt());

    }

    private static void randomString2(int i) {
        Random ran = new Random(i);
        StringBuilder builder = new StringBuilder();
        while (true){
            int k = ran.nextInt(27);
            if (k == 0){
                break;
            }
            builder.append((char) ('`' + k));
        }
        System.out.println(builder.toString());
    }
}
