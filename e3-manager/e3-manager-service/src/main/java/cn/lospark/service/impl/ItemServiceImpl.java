package cn.lospark.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.lospark.common.pojo.EasyUIDataGridResult;
import cn.lospark.jedis.JedisClient;
import cn.lospark.mapper.TbItemDescMapper;
import cn.lospark.mapper.TbItemMapper;
import cn.lospark.pojo.TbItem;
import cn.lospark.pojo.TbItemDesc;
import cn.lospark.pojo.TbItemExample;
import cn.lospark.pojo.TbItemExample.Criteria;
import cn.lospark.service.ItemService;
import cn.lospark.utils.E3Result;
import cn.lospark.utils.IDUtils;
import cn.lospark.utils.JsonUtils;

/**
 * 商品管理service
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper descMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	@Override
	public TbItem getItemById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			if (StringUtils.isNoneBlank(json)) {
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有查询数据库
		
		//根据主键查询
		//TbItem selectByPrimaryKey = itemMapper.selectByPrimaryKey(itemId);
		//设置条件进行查询。
		//1.创建查询条件对象。
		TbItemExample example =  new TbItemExample();
		Criteria criteria = example.createCriteria();
		//2.设置查询条件。
		criteria.andIdEqualTo(itemId);
		//3.执行查询
		 List<TbItem> list = itemMapper.selectByExample(example);
		 if (null != list && list.size() > 0) {
			 //把结果添加到缓存
			try {
				//添加缓存
				 jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
				 //设置过期时间
				 jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult<TbItem> getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//获取结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult<TbItem> easyUIData = new EasyUIDataGridResult<>();
		easyUIData.setRows(list);
		easyUIData.setTotal(pageInfo.getTotal());
		
		return easyUIData;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		// 生成商品ID
		final long itemId = IDUtils.genItemId();
		//补全Item的属性
		item.setId(itemId);
		//1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向商品描述表插入数据 
		descMapper.insert(itemDesc);
		/**发送商品添加消息 **/
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId + "");
			}
		});
		//返回
		return E3Result.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			if (StringUtils.isNoneBlank(json)) {
				TbItemDesc ibItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return ibItemDesc;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有查询数据库
		TbItemDesc tbItemDesc = descMapper.selectByPrimaryKey(itemId);
		 //把结果添加到缓存
		try {
			//添加缓存
			 jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(tbItemDesc));
			 //设置过期时间
			 jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
