package cn.lospark.order.pojo;

import java.io.Serializable;
import java.util.List;

import cn.lospark.pojo.TbOrder;
import cn.lospark.pojo.TbOrderItem;
import cn.lospark.pojo.TbOrderShipping;

public class OrderInfo extends TbOrder implements Serializable{
	private static final long serialVersionUID = -1871764277953863204L;
	
	private List<TbOrderItem> orderItems;
	
	private TbOrderShipping orderShipping;

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
}
