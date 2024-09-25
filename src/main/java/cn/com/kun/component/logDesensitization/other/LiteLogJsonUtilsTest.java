package cn.com.kun.component.logDesensitization.other;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.component.logDesensitization.litelog.LiteLogJsonUtils;

public class LiteLogJsonUtilsTest {

    public static void main(String[] args) {


        Student student = new Student();
        System.out.println(LiteLogJsonUtils.toJSONString(student));

    }
}
