package cn.et;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 用户的前端，视图，界面，也叫接入层 发现服务
 */
@SpringBootApplication
// 发现客户端
@EnableDiscoveryClient
public class UserViewMain {

	@LoadBalanced // 启动负载均衡,加了这个注解会自动负载均衡
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(UserViewMain.class, args);
	}

}
