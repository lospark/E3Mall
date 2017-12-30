package cn.lospark.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.lospark.pojo.TbUser;
import cn.lospark.sso.service.TokenService;
import cn.lospark.utils.CookieUtils;
import cn.lospark.utils.E3Result;

/**
 *  用户登录拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 前处理，在执行Handeller方法之前执行(顺序调用)
		// 返回true 执行，false：拦截
		//1.从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//2.没有token，直接放行
		if (StringUtils.isBlank(token)) {
			return true;
		}
		//3.获取到token，调用sso的服务，根据token取用户信息
		E3Result result = tokenService.getUserByToken(token);
		//4.没有用户信息 token 过期，直接放行
		if (result.getStatus() != 200) {
			return true;
		}
		TbUser user = (TbUser) result.getData();
		//5.取到用户信息，说明用户登录，将用户信息放入request中。后面只需要在Controller中判断request是否包含user信息即可，放行
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv)
			throws Exception {
		// Handler 执行之后，返回ModelAndView 之前

	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 完成处理，返回ModelAndView之后
		//可以在此处理异常
	}

}
