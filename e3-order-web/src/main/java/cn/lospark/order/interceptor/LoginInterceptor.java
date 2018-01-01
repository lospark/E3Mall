package cn.lospark.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.lospark.cart.service.CartService;
import cn.lospark.pojo.TbItem;
import cn.lospark.pojo.TbUser;
import cn.lospark.sso.service.TokenService;
import cn.lospark.utils.CookieUtils;
import cn.lospark.utils.E3Result;
import cn.lospark.utils.JsonUtils;

/**
 * 用户登录拦截器
 * @author Administrator
 *
 */

public class LoginInterceptor implements HandlerInterceptor{

	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		// 判断token 是否存在
		if (StringUtils.isBlank(token)) {
			//token 不存在，未登录状态，跳转到sso的登录页面，用户登录成功后，跳转到当前请求url
			response.sendRedirect(SSO_URL + "/page/login?redirect" + request.getRequestURI());
			//拦截
			return false;
		}
		//token存在，根据调用sso服务，根据token取用户信息
		E3Result result = tokenService.getUserByToken(token);
		//取不到登录已过期，需要重新登录
		if (result.getStatus() != 200) {
			response.sendRedirect(SSO_URL + "/page/login?redirect" + request.getRequestURI());
			//拦截
			return false;
		}
		TbUser user = (TbUser) result.getData();
		//如果取到用户信息，说明用户是登录状态，需要把用户信息写入request域中，
		request.setAttribute("user", user);
		//判断cookie中是否有购物车数据，如果有就合并到服务端
		String cartListJSON = CookieUtils.getCookieValue(request, "cart",true);
		//合并完成后 放行 
		if (StringUtils.isNotBlank(cartListJSON)) {
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cartListJSON, TbItem.class));
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
