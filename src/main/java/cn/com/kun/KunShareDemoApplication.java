package cn.com.kun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.web.servlet.WebMvcMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.query.QueryEnhancerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


@SpringBootApplication
//@SpringBootApplication(exclude= {WebMvcMetricsAutoConfiguration.class, TomcatMetricsAutoConfiguration.class})
public class KunShareDemoApplication {

	public static void main(String[] args) {

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
		SpringApplication.run(KunShareDemoApplication.class, args);
	}

}
