package cn.com.kun.foo.javacommon.string.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPattern3 {

    public static void main(String[] args) {

        String source = "xxxx心跳所用耗时：11111毫秒xxxxxxxxx心跳所用耗时：2222毫秒xxx";
        Pattern pattern = Pattern.compile("心跳所用耗时：(.*?)毫秒");
        Matcher matcher = pattern.matcher(source);
        while(matcher.find()) {
            //group() 默认就是 group(0)
            //假如输出，包含 心跳所用耗时 毫秒 等，就是group(0)
            //只输出匹配的串，例如 11111 2222，就是 matcher.group(1)
            System.out.println(matcher.group(1));
        }

    }


}
