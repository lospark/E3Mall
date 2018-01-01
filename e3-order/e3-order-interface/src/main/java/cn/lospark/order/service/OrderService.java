package cn.lospark.order.service;

import cn.lospark.order.pojo.OrderInfo;
import cn.lospark.utils.E3Result;

public interface OrderService {
	
	E3Result createOrder(OrderInfo orderInfo) ;

}
