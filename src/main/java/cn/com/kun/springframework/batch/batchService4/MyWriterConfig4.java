package cn.com.kun.springframework.batch.batchService4;

import cn.com.kun.bean.entity.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyWriterConfig4 {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    //定义一个写操作
    @Bean("job4Writer4ForCustom")
    public MyBatisBatchItemWriter<User> job4Writer4ForCustom(){

        //使用Mybatis提供的写操作类
        MyBatisBatchItemWriter<User> writer = new MyBatisBatchItemWriter<>();
        //这个在xml文件里定义的插入语句的id,必须全局唯一（建议加上命名空间，更加具体）
        writer.setStatementId("cn.com.kun.mapper.UserMapper.insert");
        writer.setSqlSessionFactory(sqlSessionFactory);
        return writer;
    }
}
