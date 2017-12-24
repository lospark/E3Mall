package cn.lospark.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lospark.sso.service.LoginService;
import cn.lospark.utils.CookieUtils;
import cn.lospark.utils.E3Result;

/**
 * 用户登录处理
 * <p>Title: LoginController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@Value("${TOKEN}")
	private String TOKEN;
	
	@RequestMapping("/page/login")
	public String showLogin() {
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login (String username,String password,
			HttpServletRequest request,HttpServletResponse response){
		E3Result result = loginService.userLogin(username, password);
		//判断是否登录成功
		if (result.getStatus() == 200) {
			String token = result.getData().toString();
			//把token写入cookie
			CookieUtils.setCookie(request, response, TOKEN, token);
		}
		return result;
	}
}
