package cn.com.kun.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 *
 * author:xuyaokun_kzx
 * date:2024/2/8
 * desc:
*/
@Component
public class DesedeUtils implements InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(DesedeUtils.class);

    private static DesedeUtils desedeUtils = null;

    private static final String charset = "UTF-8";

    @Value("${kunghsu.des.key}")
    private String key;

    @Override
    public void afterPropertiesSet() throws Exception {
        desedeUtils = this;
    }

    /**
     * 单例
     *
     * @return
     */
    public static DesedeUtils getInstance(){
        return desedeUtils;
    }

    public String encrptStringBase64(String source){
        return encrptStringBase64(source, key);
    }

    public String decryptStringBase64(String source){
        return decryptStringBase64(source, key);
    }


    public String getKey(){
        return key;
    }

    public static String encrptStringBase64(String source, String key) {

        try {
            BASE64Decoder dec = new BASE64Decoder();
            byte[] keyByte = dec.decodeBuffer(key);
            //构造密钥
            SecretKeySpec k = new SecretKeySpec(keyByte, "DESede");
            Cipher cp = Cipher.getInstance("DESede");
            cp.init(Cipher.ENCRYPT_MODE, k);
            byte[] b = source.getBytes(charset);
            //加密数据
            byte[] b2 = cp.doFinal(b);
            //加密后编码
            BASE64Encoder enc = new BASE64Encoder();
            return enc.encode(b2);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static String decryptStringBase64(String source, String key) {

        try {
            BASE64Decoder dec = new BASE64Decoder();
            byte[] keyByte = dec.decodeBuffer(key);
            //构建密钥
            SecretKeySpec k = new SecretKeySpec(keyByte, "DESede");
            Cipher cp = Cipher.getInstance("DESede");
            cp.init(Cipher.DECRYPT_MODE, k);
            byte[] c = dec.decodeBuffer(source);
            //解密数据
            byte[] b = cp.doFinal(c);
            return new String(b, charset);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        // 生成key
        System.out.println(SecretKeyUtils.generate3DESKey());
        BASE64Encoder enc = new BASE64Encoder();
        String key = enc.encode("kC51ksmLatQWLlfrAnm3g8eF".getBytes(StandardCharsets.UTF_8));
        //放到配置文件中的key还要过一道base64
        System.out.println(key);
        System.out.println(DesedeUtils.encrptStringBase64("kunghsu", "a0M1MWtzbUxhdFFXTGxmckFubTNnOGVG"));
        System.out.println(DesedeUtils.decryptStringBase64(encrptStringBase64("kunghsu", "a0M1MWtzbUxhdFFXTGxmckFubTNnOGVG"),
                "a0M1MWtzbUxhdFFXTGxmckFubTNnOGVG"));

    }


}
