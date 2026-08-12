package spring.ai.example.spring_ai_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringAiDemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext run = SpringApplication.run(SpringAiDemoApplication.class, args);
		Demo bean = run.getBean(Demo.class);
		System.out.println(bean.chat("你好"));
	}

}
