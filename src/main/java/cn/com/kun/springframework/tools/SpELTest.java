package cn.com.kun.springframework.tools;

import cn.com.kun.bean.model.StudentReqVO;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpELTest {


    public static void main(String[] args) {

        // 定义Parser，可以定义全局的parser
        ExpressionParser parser = new SpelExpressionParser();

        // 注意！属性名的第一个字母不区分大小写。 birthdate.year等效于Birthdate.Year
        // 取出Inventor 中，birthdate属性的year属性
        StudentReqVO zhangsan = new StudentReqVO();
        zhangsan.setIdCard("kunghsu");
        // 定义StandardEvaluationContext ，传入一个操作对象
        StandardEvaluationContext context = new StandardEvaluationContext(zhangsan);
        //计算得到布尔值的例子
        boolean flag = (boolean) parser.parseExpression("idCard.length() > 3").getValue(context);
        // true
        System.out.println(flag);

    }


}
