package cn.lospark.service;

import cn.lospark.common.pojo.EasyUIDataGridResult;
import cn.lospark.pojo.TbItem;
import cn.lospark.pojo.TbItemDesc;
import cn.lospark.utils.E3Result;

public interface ItemService {
	
	TbItem getItemById(long itemId);
	
	EasyUIDataGridResult<TbItem>  getItemList(int page,int rows); 
	
	E3Result addItem(TbItem item,String desc);
	
	TbItemDesc getItemDescById(long itemId);
}
