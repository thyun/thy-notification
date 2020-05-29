package com.thy.notification;

import java.util.Arrays;

import com.thy.notification.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
 * 
 */
@SpringBootApplication
public class MainApplication {

	private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
		
	public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MainApplication.class, args);

//        printLog(ctx);
    }

    private static void printLog(ApplicationContext ctx) {
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            logger.info(beanName);
        }

        logger.info("start");
        Config config = (Config) ctx.getBean("config");
        logger.info("servers=" + config.getServers());
    }

    @Bean
    public WebMvcConfigurerAdapter adapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/login")
                        .setViewName("login");
            }
        };
    }
}
