package cn.lospark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 内容管理
 * @author Administrator
 *
 */
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lospark.content.service.ContentService;
import cn.lospark.pojo.TbContent;
import cn.lospark.utils.E3Result;
@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent tbContent) {
		E3Result result = contentService.addContent(tbContent);
		return result;
	}

}
