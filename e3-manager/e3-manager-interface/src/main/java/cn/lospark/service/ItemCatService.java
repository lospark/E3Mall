package cn.lospark.service;

import java.util.List;

import cn.lospark.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	public List<EasyUITreeNode> getItemCatList(long parentId); 

}
