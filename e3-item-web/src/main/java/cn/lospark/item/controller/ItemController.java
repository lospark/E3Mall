package cn.lospark.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.lospark.item.pojo.Item;
import cn.lospark.pojo.TbItem;
import cn.lospark.pojo.TbItemDesc;
import cn.lospark.service.ItemService;

/**
 * 商品详情页面展示controller
 * @author Administrator
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;

	@RequestMapping(value="/item/{itemId}",method=RequestMethod.POST)
	public String showItemInfo(@PathVariable("itemId") long itemId,Model model) {
		// 调用服务取出商品基本信息、商品描述信息，并传递给页面，返回逻辑视图
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
	

}
