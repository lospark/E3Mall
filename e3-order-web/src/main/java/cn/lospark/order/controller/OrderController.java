package cn.lospark.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.lospark.cart.service.CartService;
import cn.lospark.order.pojo.OrderInfo;
import cn.lospark.order.service.OrderService;
import cn.lospark.pojo.TbItem;
import cn.lospark.pojo.TbUser;
import cn.lospark.utils.E3Result;

/**
 * 订单管理controller
 * @author Administrator
 *
 */
@Controller
public class OrderController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		TbUser user = (TbUser) request.getAttribute("user");
		// 取出购物车列表
		List<TbItem> cartList = cartService.getCartList(user.getId());
		//根据用户Id取收货地址列表
		//使用静态数据
		//取出支付方式
		
		request.setAttribute("cartList", cartList);
		
		return "order-cart";
	}
	
	@RequestMapping(value="order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		//取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//把用户信息添加orderInfo中
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//调用服务生成订单
		E3Result result = orderService.createOrder(orderInfo);
		//如果订单生成成功，需要删除购物车
		if (result.getStatus() == 200) {
			//清空购物车
			cartService.clearCartItem(user.getId());
		}
		//把订单号传递给页面
		request.setAttribute("orderId",result.getData());
		request.setAttribute("payment",orderInfo.getPayment());
		//返回一个逻辑视图
		return "success";
	}

}
