package cn.lospark.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lospark.pojo.TbUser;
import cn.lospark.sso.service.RegisterService;
import cn.lospark.utils.E3Result;

/**
 * 注册功能
 * @author Administrator
 *
 */
@Controller
public class RegisterController { 
	
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping(value="/page/register")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public  E3Result checkData(@PathVariable String param,@PathVariable Integer type ){
		E3Result checkData = registerService.checkData(param, type);
		return checkData;
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		return registerService.register(user);
	}
	

}
