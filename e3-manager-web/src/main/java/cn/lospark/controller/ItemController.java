package cn.lospark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lospark.common.pojo.EasyUIDataGridResult;
import cn.lospark.pojo.TbItem;
import cn.lospark.service.ItemService;
import cn.lospark.utils.E3Result;

/** 
 * 商品管理
 * @author Administrator
 *
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemByid(@PathVariable long itemId){
		return itemService.getItemById(itemId);
	}
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult<TbItem> getItemList(Integer page,Integer rows){
		//调用服务查询列表	
		EasyUIDataGridResult<TbItem> itemList = itemService.getItemList(page, rows);
		return itemList; 
	}
	/**
	 * 商品功能添加
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item , String desc) {
		E3Result result = itemService.addItem(item, desc);
		return result;  
	}
	
	
	
}
