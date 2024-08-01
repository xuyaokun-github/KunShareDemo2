package cn.com.kun.springframework.core.jackson.deserializationProblem;

import cn.com.kun.common.utils.JacksonUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 强化版Jackson反序列化工具类--特殊字符实验
 *
 * author:xuyaokun_kzx
 * date:2024/7/30
 * desc:
*/
public class TestSupportSpecialChar {


    public static void main(String[] args) throws IOException {

//        testMethod1();
//        testMethod2();
//        testMethod3();
//        testMethod4();
//        testMethod5();
        testMethod6();

    }


    /**
     * good6.txt里的内容是：
     * aaa:::sjklss123,456\\"","78999'999
     *
     * @throws IOException
     */
    private static void testMethod6() throws IOException {

        String goodDesc = FileUtils.readFileToString(new File("D:\\home\\json\\good6.txt"), Charset.forName("UTF-8"));
        Map<String, String> map = new HashMap<>();
        map.put("name", "kunghsu");
        map.put("address", "shenzhen");
        map.put("goodDesc", "$goodDesc$");
        //模板json串
        String templateJson = JacksonUtils.toJSONString(map);
        System.out.println(templateJson);
        System.out.println("上游系统送的goodDesc：" + goodDesc);
        //在做模板replace替换之前，就对参数值做了特殊字符替换
        goodDesc = goodDesc.replace("\\", "\\" + "\\");
        goodDesc = goodDesc.replace("\"", "\\" + "\"");

        templateJson = templateJson.replace("$goodDesc$", goodDesc);
        System.out.println("替换后, 反序列化源串：" + templateJson);

        Map<String, Object> resultMap = JacksonUtils.toMapEnhancedVersion(templateJson);
        System.out.println("输出结果Map内容：");
        System.out.println(resultMap);
        System.out.println("最终下游系统拿到的goodDesc：" + resultMap.get("goodDesc"));

    }

    /**
     * good5.txt里的内容是：
     * aaasjklss123,45678999999 这是没问题的，正常的json反序列化是支持逗号的
     *
     * aaasj"""""klss123,45678999999  这种情况同时存在双引号和逗号
     * aaa:::sj"""""klss123,45678999999  这种情况同时存在双引号和逗号、冒号
     * aaa:::sj"""""klss\123,456\789\99999  这种情况同时存在双引号和逗号、冒号、反斜杠*
     * aaa:::sj"""""klss\123,456\789\99'999  这种情况同时存在双引号和逗号、冒号、反斜杠、单引号
     * aaa:::sj"""""klss\123","456\789\99'999 这种暂时处理不了
     *
     * 这个例子同时存在两类问题，一个是双引号问题，一个是反斜杠问题
     *
     * @throws IOException
     */
    private static void testMethod5() throws IOException {

        String goodDesc = FileUtils.readFileToString(new File("D:\\home\\json\\good5.txt"), Charset.forName("UTF-8"));
        Map<String, String> map = new HashMap<>();
        map.put("name", "kunghsu");
        map.put("address", "shenzhen");
        map.put("goodDesc", "$goodDesc$");
        //模板json串
        String templateJson = JacksonUtils.toJSONString(map);
        System.out.println(templateJson);
        templateJson = templateJson.replace("$goodDesc$", goodDesc);
        System.out.println("替换后, 反序列化源串：" + templateJson);

        Map<String, Object> resultMap = JacksonUtils.toMapEnhancedVersion(templateJson);
        System.out.println("输出结果Map内容：");
        System.out.println(resultMap);
        System.out.println("上游系统送的goodDesc：" + goodDesc);
        System.out.println("最终下游系统拿到的goodDesc：" + resultMap.get("goodDesc"));
    }


    /**
     * good4.txt里的内容是：
     * aaa:::sj"""""kl'ss\123456\789\99999
     *
     * 这个例子同时存在两类问题，一个是双引号问题，一个是反斜杠问题
     *
     * @throws IOException
     */
    private static void testMethod4() throws IOException {

        String goodDesc = FileUtils.readFileToString(new File("D:\\home\\json\\good4.txt"), Charset.forName("UTF-8"));
        Map<String, String> map = new HashMap<>();
        map.put("name", "kunghsu");
        map.put("address", "shenzhen");
        map.put("goodDesc", "$goodDesc$");
        //模板json串
        String templateJson = JacksonUtils.toJSONString(map);
        System.out.println(templateJson);
        templateJson = templateJson.replace("$goodDesc$", goodDesc);
        System.out.println("替换后, 反序列化源串：" + templateJson);

        Map<String, Object> resultMap = JacksonUtils.toMapEnhancedVersion(templateJson);
        System.out.println("输出结果Map内容：");
        System.out.println(resultMap);
        System.out.println("上游系统送的goodDesc：" + goodDesc);
        System.out.println("最终下游系统拿到的goodDesc：" + resultMap.get("goodDesc"));

    }


    /**
     * 复现问题： com.fasterxml.jackson.core.JsonParseException: Unexpected character ('k' (code 107)): was expecting comma to separate Object entries
     *
     * @throws IOException
     */
    private static void testMethod1() throws IOException {

        String goodDesc = FileUtils.readFileToString(new File("D:\\home\\json\\good1.txt"), Charset.forName("UTF-8"));
        Map<String, String> map = new HashMap<>();
        map.put("name", "kunghsu");
        map.put("address", "shenzhen");
        map.put("goodDesc", "$goodDesc$");
        //模板json串
        String templateJson = JacksonUtils.toJSONString(map);
        System.out.println(templateJson);
        System.out.println("替换后");
        templateJson = templateJson.replace("$goodDesc$", goodDesc);

        Map<String, Object> resultMap = JacksonUtils.toMap(templateJson);
        System.out.println("输出结果Map内容：");
        System.out.println(resultMap);

    }

    /**
     * good2.txt里的内容是：
     * aaa:sj"kl'ss123456
     *
     * @throws IOException
     */
    private static void testMethod2() throws IOException {


        String goodDesc = FileUtils.readFileToString(new File("D:\\home\\json\\good2.txt"), Charset.forName("UTF-8"));
        Map<String, String> map = new HashMap<>();
        map.put("name", "kunghsu");
        map.put("address", "shenzhen");
        map.put("goodDesc", "$goodDesc$");
        //模板json串
        String templateJson = JacksonUtils.toJSONString(map);
        System.out.println(templateJson);
        templateJson = templateJson.replace("$goodDesc$", goodDesc);
        System.out.println("替换后, 反序列化源串：" + templateJson);

        Map<String, Object> resultMap = JacksonUtils.toMapSupportSpecialChar(templateJson);
        System.out.println("输出结果Map内容：");
        System.out.println(resultMap);
        System.out.println("上游系统送的goodDesc：" + goodDesc);
        System.out.println("最终下游系统拿到的goodDesc：" + resultMap.get("goodDesc"));

    }

    /**
     * good3.txt里的内容是：
     * aaa:::sj"""""kl'ss123456
     *
     * @throws IOException
     */
    private static void testMethod3() throws IOException {

        String goodDesc = FileUtils.readFileToString(new File("D:\\home\\json\\good3.txt"), Charset.forName("UTF-8"));
        Map<String, String> map = new HashMap<>();
        map.put("name", "kunghsu");
        map.put("address", "shenzhen");
        map.put("goodDesc", "$goodDesc$");
        //模板json串
        String templateJson = JacksonUtils.toJSONString(map);
        System.out.println(templateJson);
        templateJson = templateJson.replace("$goodDesc$", goodDesc);
        System.out.println("替换后, 反序列化源串：" + templateJson);

        Map<String, Object> resultMap = JacksonUtils.toMapEnhancedVersion(templateJson);
        System.out.println("输出结果Map内容：");
        System.out.println(resultMap);
        System.out.println("上游系统送的goodDesc：" + goodDesc);
        System.out.println("最终下游系统拿到的goodDesc：" + resultMap.get("goodDesc"));

    }

}
