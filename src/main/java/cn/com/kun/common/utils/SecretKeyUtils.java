package cn.com.kun.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName: SecretKeyUtils
 * 各秘钥生成
 */
public class SecretKeyUtils {

    /** AES秘钥 */
    private static final String AES_ALGORITHM = "AES";

    /** 3DES秘钥 */
    private static final String DES_ALGORITHM = "DESede";

    /** 秘钥生成来源 */
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 生成AES密钥对象
     * @throws NoSuchAlgorithmException
     */
    public static String generateAESKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 生成3DES密钥对象
     */
    public static String generate3DESKey() {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 24; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 随机生成RSA秘钥
     * @throws NoSuchAlgorithmException
     */
    public static Map<String,String > genKeyPair() throws NoSuchAlgorithmException {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.getEncoder().encodeToString((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        //0表示公钥
        stringStringHashMap.put("0",publicKeyString);
        //1表示私钥
        stringStringHashMap.put("1",privateKeyString);
        return stringStringHashMap;
    }

}
