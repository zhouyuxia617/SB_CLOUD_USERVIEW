package cn.et.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	/**
	 * 登录用户
	 */
	//@RequestMapping("/validUser")
	@GetMapping("/validUser")
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
	
	
	/**
	 * 添加用户，使用rest风格
	 */
	@PostMapping("/user")
	public String validUser(@RequestParam Map map) {
		
		//模拟一个请求头
		HttpHeaders requestHeaders = new HttpHeaders();
		//代表传递的是json
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		//代表返回可接受的json类型
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		//将请求头和请求体(表单数据)打包
		HttpEntity<Map> request = new HttpEntity<Map>(map,requestHeaders);
		
		//第一个调用微服务的路径，第二个是请求，第三个是返回的响应类型
		String returnCode = template.postForObject("http://USERSERVICE/user", request, String.class);
		
		if(returnCode.equals("1")) {
			return "/suc.jsp";
		}else {
			return "reg.jsp";
		}
	}
	
	
	/**
	 * 修改用户，rest风格
	 */
	@PutMapping(value="/user/{userId}")
	public String validUser(@PathVariable String userId,@RequestParam Map map) {
		
		//模拟请求头
		HttpHeaders requestHeaders = new HttpHeaders();
		//代表传递的是json类型
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		//代表返回可接受json类型
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		//将请求头和请求体（表单数据）打包
		HttpEntity<Map> request = new HttpEntity<Map>(map,requestHeaders);
		
		//第一个调用微服务的路径，第二个是请求，第三个是返回的响应类型
	//	String returnCode = template.postForObject("http://USERSERVICE/user1/{userId}"+userId, request, String.class);
		Map variables = new HashMap();
		variables.put("userId", userId);
	//	ResponseEntity<String> postForEntity = template.postForEntity("http://USERSERVICE/user/{userId}"+userId, request, String.class,variables);		
		template.put("http://USERSERVICE/user/{userId}", request,variables);
	//	String returnCode = postForEntity.getBody();
		String returnCode = "1";
		
		if(returnCode.equals("1")) {
			return "/suc.jsp";
		}else {
			return "update.jsp";
		}
	}
	
	/**
	 * 删除用户，rest风格
	 */
	@DeleteMapping(value="/user/{userId}")
	public String updateUser(@PathVariable String userId) {
		//模拟请求头
		Map variables = new HashMap();
		variables.put("userId", userId);
		
		template.delete("http://USERSERVICE/user/{userId}",variables);
		String returnCode = "1";
		
		if(returnCode.equals("1")) {
			return "suc.jsp";
		}else {
			return "del.jsp";
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
