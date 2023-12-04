package cn.com.kun.foo.javacommon.datatype.enumtype.demo1;

public class KeyboardConfigTest {

    public static void main(String[] args) {

        KeyboardConfig keyboardConfig = new KeyboardConfig();
        System.out.println(keyboardConfig.isEnabled(KeyboardFeature.Q_VALUE));
        System.out.println(keyboardConfig.isEnabled(KeyboardFeature.E_VALUE));
        System.out.println(keyboardConfig.isEnabled(KeyboardFeature.W_VALUE));
        System.out.println(keyboardConfig.isEnabled(KeyboardFeature.R_VALUE));

    }
}
