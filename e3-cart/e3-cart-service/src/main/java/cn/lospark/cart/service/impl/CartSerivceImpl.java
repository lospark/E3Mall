package cn.lospark.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.lospark.cart.service.CartService;
import cn.lospark.jedis.JedisClient;
import cn.lospark.mapper.TbItemMapper;
import cn.lospark.pojo.TbItem;
import cn.lospark.utils.E3Result;
import cn.lospark.utils.JsonUtils;

/**
 * 购物车处理服务
 * @author Administrator
 *
 */

@Service
public class CartSerivceImpl implements CartService {

	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	
	@Autowired
	private JedisClient  jedisClient;
	@Autowired 
	private TbItemMapper itemMapper;
	
	@Override
	public E3Result addCart(long userId, long itemId,int num ) {
		//向redis中添加购物车
		//数据类型食hash，key：用户Id field：商品Id  value： 商品信息
		//如果存在 ，数量相加，如果不存在 添加
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId, itemId +"");
		//判断商品是否存在
		if (hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId +"");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum() + num);
			//写回redis
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId +"", JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		// 不存在时的处理
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		//取一张图片
		String image = tbItem.getImage();
		if (StringUtils.isNotBlank(image)) {
			tbItem.setImage(image.split(",")[0]);
		}
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId +"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		//遍历商品列表
		//把列表添加到购物车（重复数量相加，没有添加一个新商品）
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		//根据id查询购物车列表
		List<String> jsonlist = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> list = new ArrayList<>();
		for (String json : jsonlist) {
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			list.add(item);
		}
		return list;
	}

	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId + "");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId +"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		// TODO Auto-generated method stub
		return null;
	}

}
