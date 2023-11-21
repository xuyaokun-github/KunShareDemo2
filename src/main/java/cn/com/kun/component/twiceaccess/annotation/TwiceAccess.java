package cn.com.kun.component.twiceaccess.annotation;

import java.lang.annotation.*;


/**
 * 二次访问组件注解
 *
 * author:xuyaokun_kzx
 * date:2023/11/21
 * desc:
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TwiceAccess {


    /**
     * 等待时间，默认是5000ms
     * @return
     */
    long waitTime() default 5000;


}
