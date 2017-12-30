package cn.lospark.cart.service;

import java.util.List;

import cn.lospark.pojo.TbItem;
import cn.lospark.utils.E3Result;

public interface CartService {

	E3Result addCart(long userId,long itemId,int num);
	
	E3Result mergeCart(long userId, List<TbItem> itemList);
	
	List<TbItem> getCartList(long userId);
	
	E3Result updateCartNum(long userId,long itemId,int num);
	
	E3Result deleteCartItem(long userId,long itemId);
}
