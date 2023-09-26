package cn.com.kun.springframework.batch.batchService3;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * batch demo3
 * 例子：步骤间的状态转移
 *
 * author:xuyaokun_kzx
 * date:2021/5/21
 * desc:
 */
@Configuration
public class BatchDemo3JobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step stepOne(){
        return stepBuilderFactory.get("myJob3StepOne")
                .tasklet(new MyTaskOne())
                .build();
    }

    @Bean
    public Step stepTwo(){
        return stepBuilderFactory.get("myJob3StepTwo")
                .tasklet(new MyTaskTwo())
                .build();
    }

    @Bean
    public Step stepThree(){
        return stepBuilderFactory.get("myJob3StepThree")
                .tasklet(new MyTaskThree())
                .build();
    }


    @Bean
    public Job myJob3(){

        //顺序执行，没有状态转移判断
//        return jobBuilderFactory.get("demoJob3")
//                .incrementer(new RunIdIncrementer())
//                .start(stepOne())
//                .next(stepTwo()) //可以定义多个步骤，链式调用next方法即可
//                .next(stepThree())
//                .build();

        //简单的状态转移
        /*
        假如上一步抛出异常，没有return RepeatStatus.FINISHED，就不会进行到下一步
         */
        //注意不要填错定义成一个循环，会进入死循环，例如1-》2,2—》1
        return jobBuilderFactory.get("myJob3")
                .start(stepOne())
                .on("COMPLETED").to(stepTwo())//假如第一步成功后才会走到下一步，第二步
                .on("FAILED").to(stepThree()) //假如第一步失败，就直接跳到第三步
                .from(stepTwo())
                .on("COMPLETED").to(stepThree())
                .from(stepThree()).end().build();
    }

}
