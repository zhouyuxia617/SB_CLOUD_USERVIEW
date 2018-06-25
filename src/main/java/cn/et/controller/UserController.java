package cn.et.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * 在我的控制层调用微服务的控制层
 */
//@RestController 使用这个注解会将String类型返回成一个json
@Controller //不想跳转成json就使用这个注解
public class UserController {

	@Autowired
	private RestTemplate template;
	
	@RequestMapping("/validUser")
	public String validUser(String userName,String password) {
		
		//这个地方调的是微服务，一台挂了可以调用另一台，可以负载均衡
		String returnCode = template.getForObject("http://USERSERVICE/validUser?userName="+userName+"&password="+password, String.class);
		
		//登录成功
		if(returnCode.equals("1")) {
			return "/suc.jsp";
		}else { //登录失败跳回到登录界面
			return "/login.jsp";
		}
	}
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	/**
	 * 启动多个 发布者，端口不一致，程序名相同
	 */
	@ResponseBody //想要返回json使用这个注解
	@RequestMapping("choosePub")
	public String choosePub() {
		
		StringBuffer sb = new StringBuffer();
		
		//选择十次，轮询算法，负载均衡，轮着来
		for(int i=0; i<=10; i++) {
			//从两个idserver中选择一个，这里涉及到选择算法
			ServiceInstance ss = loadBalancer.choose("USERSERVICE");
			
			sb.append(ss.getUri().toString()+"<br/>");
		}
		
		return sb.toString();
	}
	
}
