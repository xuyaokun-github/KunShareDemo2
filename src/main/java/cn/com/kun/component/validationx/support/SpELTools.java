package cn.com.kun.component.validationx.support;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpELTools {

    public static boolean parseExpressionToBoolean(Object bean, String expression){
        // 定义Parser，可以定义全局的parser
        ExpressionParser parser = new SpelExpressionParser();
        // 注意！属性名的第一个字母不区分大小写
        // 定义StandardEvaluationContext ，传入一个操作对象
        StandardEvaluationContext context = new StandardEvaluationContext(bean);
        //计算得到布尔值的例子
        boolean flag = (boolean) parser.parseExpression(expression).getValue(context);
        return flag;
    }

}
