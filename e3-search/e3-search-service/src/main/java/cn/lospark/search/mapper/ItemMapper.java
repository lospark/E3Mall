package cn.lospark.search.mapper;

import java.util.List;

import cn.lospark.common.pojo.SearchItem;

public interface ItemMapper {
	
	List<SearchItem>  getItemList();
	
	SearchItem getItemById(long itemId);

}
