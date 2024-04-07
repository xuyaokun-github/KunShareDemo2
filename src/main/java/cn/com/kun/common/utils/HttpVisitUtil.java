package cn.com.kun.common.utils;

import cn.com.kun.common.vo.ResultVo;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpVisitUtil {


    public static void main(String[] args) throws IOException {

        String url = "http://192.168.160.13:8080/pipes/phoenix/task/submit";
        Map<String, String> requestParamMap = new HashMap<>();
        requestParamMap.put("name", "111");
        requestParamMap.put("age", "10");
        requestParamMap.put("bean", JSON.toJSONString(ResultVo.valueOfSuccess()));

        Map<String, String> headers = new HashMap<>();
        headers.put("token", "2222");

        String result = doByPost(url, requestParamMap, headers);
        System.out.println(result);
    }

    public static String doByPost(String urlPath, Map<String, String> requestParamMap) {

        return doByPost(urlPath, requestParamMap, null);
    }

    /**
     *
     * @param urlPath
     * @param requestParamMap 设置到request请求体中的参数
     * @return
     */
    public static String doByPost(String urlPath, Map<String, String> requestParamMap, Map<String, String> headers) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            // 获取一个HttpURLConnection链接对象
            conn = (HttpURLConnection) url.openConnection();
            // 设置超时时间
            conn.setConnectTimeout(600000);
            conn.setReadTimeout(600000);
            conn.setUseCaches(false);
            // 以post方式请求
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");


            //设置请求头
            if (headers != null){
                Set<Map.Entry<String, String>> set = headers.entrySet();
                for (Map.Entry<String, String> entry : set){
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            String request = buildRequestParam(requestParamMap);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(request.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            // 返回打开连接读取的输入流
            InputStream in = conn.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            // 判断是否正常响应数据
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("网络错误异常！!!!");
            } else {
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(dis);
                BufferedOutputStream bos = new BufferedOutputStream(bao);
                int BUFFER_LEN = 2048;
                byte[] bytes = new byte[BUFFER_LEN];
                int c = 0;
                while ((c = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, c);
                    bos.flush();
                }
                String output = new String(bytes, "UTF-8");
                return output.trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    private static String buildRequestParam(Map<String, String> requestParamMap){

        StringBuilder builder = new StringBuilder();

        Set<Map.Entry<String, String>> set = requestParamMap.entrySet();
        for (Map.Entry<String, String> entry : set){
            builder.append(entry.getKey() + "=" + entry.getValue());
            builder.append("&");
        }
        String temp = builder.toString();
        return temp.length() > 0 ? temp.substring(0, temp.length() -1) : "";
    }

    public static String doByGet(String urlPath, Map<String, String> requestParamMap) {

        return doByGet(urlPath, requestParamMap, null);
    }


    public static String doByGet(String urlPath, Map<String, String> requestParamMap, Map<String, String> headers) {

        HttpURLConnection conn = null;
        try {
            String requestParam = buildRequestParam(requestParamMap);
            URL url = new URL(urlPath + "?" + requestParam);
            // 获取一个HttpURLConnection链接对象
            conn = (HttpURLConnection) url.openConnection();
            // 设置超时时间
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setUseCaches(false);
            // 以get方式请求
            conn.setRequestMethod("GET");

            //设置请求头
            if (headers != null){
                Set<Map.Entry<String, String>> set = headers.entrySet();
                for (Map.Entry<String, String> entry : set){
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 返回打开连接读取的输入流
            InputStream in = conn.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            // 判断是否正常响应数据
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("网络错误异常！!!!");
            } else {
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(dis);
                BufferedOutputStream bos = new BufferedOutputStream(bao);
                int BUFFER_LEN = 2048;
                byte[] bytes = new byte[BUFFER_LEN];
                int c = 0;
                while ((c = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, c);
                    bos.flush();
                }
                String output = new String(bytes, "UTF-8");
                System.out.println(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null){
                conn.disconnect();
            }
        }
        return null;
    }
}
