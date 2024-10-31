package cn.com.kun.service.tthawk;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.tthawk.core.TthawkBase64Utils;
import cn.com.kun.service.tthawk.vo.TthawkDemoEnum;
import cn.com.kun.service.tthawk.vo.TthawkDemoVO1;
import cn.com.kun.service.tthawk.vo.TthawkDemoVO2;

public class Base64JdkImplUtilsTest {

    public static void main(String[] args) {

        TthawkDemoVO1 tthawkDemoVO1 = new TthawkDemoVO1();
        tthawkDemoVO1.setName("kunghsu");
        System.out.println(TthawkBase64Utils.encrypt(JacksonUtils.toJSONString(tthawkDemoVO1)));
        TthawkDemoVO2 tthawkDemoVO2 = new TthawkDemoVO2();
        tthawkDemoVO2.setName("kunghsu2");
        System.out.println(TthawkBase64Utils.encrypt(JacksonUtils.toJSONString(tthawkDemoVO2)));


        System.out.println(TthawkBase64Utils.encrypt(JacksonUtils.toJSONString(TthawkDemoEnum.FAILED)));

//        System.out.println(TthawkBase64Utils.encrypt(JacksonUtils.toJSONString(new TthawkDemoVO3("kunghsu"))));

    }
}
