package org.springframework.social.showcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "org.springframework.social.showcase.model")
@EnableJpaRepositories(basePackages = "org.springframework.social.showcase.repository")
@EnableTransactionManagement
@EnableFeignClients(basePackages="technology.touchmars.feign.wechat.client.api")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}