package cn.lospark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lospark.common.pojo.EasyUITreeNode;
/**
 *条目节点管理
 * @author Administrator
 *
 */
import cn.lospark.service.ItemCatService;
@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode>  getItemcatlist(
			@RequestParam(name="id",defaultValue="0") Long parentId){
		//调用服务查询节点列表
		List<EasyUITreeNode> result = itemCatService.getItemCatList(parentId);
		return result;
	}
	
}
