package cn.lospark.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.lospark.common.pojo.SearchResult;
import cn.lospark.search.service.SearchService;

/**
 * 商品搜索
 * @author Administrator
 *
 */
@Controller
public class SearchController {
	
	@Value("${SEARCH_RESUILT_ROWS}")
	private Integer rows;
	
	@Autowired
	private SearchService  searchService;
	
	@RequestMapping("/search")
	public String searchItemList( String keyword,
			@RequestParam(defaultValue="1") Integer page,Model model) throws Exception{
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		//查询商品
		SearchResult result = searchService.search(keyword, page, rows);
		//把结果传递给页面
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recordCount", result.getRecordCount());
		model.addAttribute("itemList", result.getItemList());
		return "search";
	}

}
