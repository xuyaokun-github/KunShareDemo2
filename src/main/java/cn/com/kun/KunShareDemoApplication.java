package cn.com.kun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.query.QueryEnhancerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Springboot2.7.12 Demo
 *
 * author:xuyaokun_kzx
 * date:2023/9/26
 * desc:
*/
@SpringBootApplication
public class KunShareDemoApplication {

	public static void main(String[] args) {

		System.out.println("KunShareDemoApplication main invoke!");
		if (args != null){
			for (String arg : args){
				System.out.println("main方法接收到的参数：" + arg);
			}
		}
		disableJsqlparser();

		SpringApplication.run(KunShareDemoApplication.class, args);

//		SpringApplication application = new SpringApplication(KunShareDemoApplication.class);
//		application.addListeners(new CustomContextInitializedListener());
//		application.run(args);
	}

	private static void disableJsqlparser() {

		try {
			QueryEnhancerFactory.forQuery(null) ;
		} catch(Exception e) {
			// just init class QueryEnhancerFactory, ignore anything
		}

		try {
			Field JSQLPARSER_IN_CLASSPATH = QueryEnhancerFactory.class.getDeclaredField("JSQLPARSER_IN_CLASSPATH") ;

			Field modifierField = Field.class.getDeclaredField("modifiers") ;
			modifierField.setAccessible(true);
			modifierField.setInt(JSQLPARSER_IN_CLASSPATH, JSQLPARSER_IN_CLASSPATH.getModifiers() & ~Modifier.FINAL);

			JSQLPARSER_IN_CLASSPATH.setAccessible(true);
			//因为是静态属性，所以这里第一参数传null
			JSQLPARSER_IN_CLASSPATH.setBoolean(null, false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
