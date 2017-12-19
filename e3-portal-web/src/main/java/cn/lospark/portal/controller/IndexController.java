package cn.lospark.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lospark.content.service.ContentService;
import cn.lospark.pojo.TbContent;

/**
 * 首页展示
 * @author Administrator
 *
 */
@Controller
public class IndexController {
	
	@Value("${CONTENT_LUNBO_ID}")
	private Long  cid;
	
	@Autowired 
	private ContentService contentService;
	
	@RequestMapping("/index")
	public String showIndex(Model model) {
		//查询内容列表
		List<TbContent> ad1List = contentService.findContentListByCid(cid);
		model.addAttribute("ad1List",ad1List);
		return "index";
	}  
	
}
