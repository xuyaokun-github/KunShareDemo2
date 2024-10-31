package cn.com.kun.component.tthawk.core;

import java.util.Base64;

public class TthawkBase64Utils {

    private static Base64.Encoder ENCODER = Base64.getEncoder();
    private static Base64.Decoder DECODER = Base64.getDecoder();

    /**
     * BASE64加密
     */
    public static String encrypt(String str) {
        if (str == null) return null;
        byte[] bytes = str.getBytes();
        //Base64 加密
        String encoded = ENCODER.encodeToString(bytes);
        return encoded;
    }

    /**
     * BASE64解密
     */
    public static String decrypt(String key) {
        if (key == null) return null;
        byte[] decoded = DECODER.decode(key);
        String decodeStr = new String(decoded);
        return decodeStr;
    }

}