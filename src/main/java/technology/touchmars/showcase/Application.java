package technology.touchmars.showcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages= {"technology.touchmars.template", "technology.touchmars.showcase"})
@EntityScan(basePackages = "technology.touchmars.template.model")
@EnableJpaRepositories(basePackages = "technology.touchmars.template.repository")
@EnableTransactionManagement
@EnableFeignClients(basePackages="technology.touchmars.feign.wechat.client.api")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}