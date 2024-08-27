package cn.com.kun.foo.javacommon.exception;

/**
 * 测试主动抛出运行时异常
 *
 * author:xuyaokun_kzx
 * date:2024/8/13
 * desc:
*/
public class TestThrowInterruptedException {

    public static void main(String[] args) {

        try {
            method();
        }catch (Exception e){
            System.out.println("抛出异常信息：");
            e.printStackTrace();
        }

    }

    private static void method() {

        try {
            int a = 1/0;
        }catch (Exception e){
            if (true){
                throw new RuntimeException();
            }

        }
    }
}
