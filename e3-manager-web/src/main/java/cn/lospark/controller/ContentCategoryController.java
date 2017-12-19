package cn.lospark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.lospark.common.pojo.EasyUITreeNode;
import cn.lospark.content.service.ContentCategoryService;
import cn.lospark.utils.E3Result;


/**
 * 内容管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/content")
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name = "id",defaultValue = "0")long parentId) {
		
		List<EasyUITreeNode> contentCategoryList = contentCategoryService.getContentCategoryList(parentId);
		return contentCategoryList;
	}
	
	@RequestMapping(value="/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result createCategory(Long parentId ,String name) {
		E3Result result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	@RequestMapping(value="/category/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateCategory(Long id ,String name) {
		E3Result result = contentCategoryService.updateContentCategory(id, name);
		return result;
	}
	@RequestMapping(value="/category/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteCategory(Long id ) {
		E3Result result = contentCategoryService.deleteContentCategory(id);
		return result;
	}

}
