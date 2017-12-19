package cn.lospark.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.lospark.content.service.ContentService;
import cn.lospark.jedis.JedisClient;
import cn.lospark.mapper.TbContentMapper;
import cn.lospark.pojo.TbContent;
import cn.lospark.pojo.TbContentExample;
import cn.lospark.pojo.TbContentExample.Criteria;
import cn.lospark.utils.E3Result;
import cn.lospark.utils.JsonUtils;
/**
 * 内容管理
 * @author Administrator
 *
 */
		
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Override
	public E3Result addContent(TbContent tbContent) {
		//补全内容
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		//将内容插入到数据库
		contentMapper.insert(tbContent);
		//缓存同步，删除缓存中对应的数据
		jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
		return E3Result.ok();
	}

	@Override
	public List<TbContent> findContentListByCid(long Cid) {
		//查询缓存
		try {
			//如果缓存中有直接响应结果
			String jsonData = jedisClient.hget(CONTENT_LIST, Cid+"");
			if (StringUtils.isNotBlank(jsonData)) {
				List<TbContent> list = JsonUtils.jsonToList(jsonData, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果没有查询数据库
		//设置查询条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(Cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//把结果添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, Cid + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
