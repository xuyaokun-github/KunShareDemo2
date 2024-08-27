package cn.com.kun.service.tthawk;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.tthawk.reflect.TthawkBase64Utils;

public class Base64JdkImplUtilsTest {

    public static void main(String[] args) {

        TthawkDemoVO1 tthawkDemoVO1 = new TthawkDemoVO1();
        tthawkDemoVO1.setName("kunghsu");
        System.out.println(TthawkBase64Utils.encrypt(JacksonUtils.toJSONString(tthawkDemoVO1)));
        TthawkDemoVO2 tthawkDemoVO2 = new TthawkDemoVO2();
        tthawkDemoVO2.setName("kunghsu2");
        System.out.println(TthawkBase64Utils.encrypt(JacksonUtils.toJSONString(tthawkDemoVO2)));

    }
}
