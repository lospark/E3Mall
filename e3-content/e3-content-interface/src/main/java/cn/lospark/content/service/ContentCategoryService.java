package cn.lospark.content.service;

import java.util.List;

import cn.lospark.common.pojo.EasyUITreeNode;
import cn.lospark.utils.E3Result;

public interface ContentCategoryService {
	 
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	
	E3Result  addContentCategory(long parentId,String name); 
	
	E3Result  updateContentCategory(long id,String name); 
	
	E3Result  deleteContentCategory(long id); 
}
