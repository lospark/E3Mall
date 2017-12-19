package cn.lospark.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lospark.common.pojo.SearchResult;
import cn.lospark.search.dao.SearchDao;
import cn.lospark.search.service.SearchService;
/**
 * 商品搜索服务
 * @author Administrator
 *
 */
@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDao searchDao;
 
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		//创建查询条件（关键字和分页、默认搜索域）
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(keyword);
		if (page <= 0) page = 1;
		solrQuery.setStart((page - 1)*rows);
		solrQuery.setRows(rows);
		solrQuery.set("df", "item_title");
		//开启高亮显示、前缀和后缀
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\" >");
		solrQuery.setHighlightSimplePost("</em>");
		//调用DAO执行查询。
		SearchResult searchResult = searchDao.search(solrQuery);
		//补全总页数，并返回结果
		long recordCount = searchResult.getRecordCount();
		int totalPage = (int) ((recordCount + rows -1 )/ rows);
		searchResult.setTotalPages(totalPage);
		return searchResult;
	}

}
