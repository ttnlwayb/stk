package com.xuan;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableScheduling
@SpringBootApplication
@EnableAsync
public class Application extends SpringBootServletInitializer implements AsyncConfigurer {

    public static void main(String[] args) {
    	System.setProperty("java.awt.headless", "false");
        SpringApplication.run(Application.class, args);
    }
    
    @Override
	public ThreadPoolTaskExecutor getAsyncExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);   
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(200);
		executor.setKeepAliveSeconds(20);
		executor.setThreadNamePrefix("AsyncService-");
		
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

}
