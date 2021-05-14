package edu.miu.cs.cs544;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class Application {
	@Bean
	public ModelMapper modelMapper(){return new ModelMapper();}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);	
		}

}
