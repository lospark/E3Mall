package cn.lospark.content.service;

import java.util.List;

import cn.lospark.pojo.TbContent;
import cn.lospark.utils.E3Result;

public interface ContentService {
	
	E3Result addContent(TbContent tbContent);
	
	List<TbContent> findContentListByCid(long Cid);
}
