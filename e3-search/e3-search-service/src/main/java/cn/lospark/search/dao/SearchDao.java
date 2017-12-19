package cn.lospark.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.lospark.common.pojo.SearchItem;
import cn.lospark.common.pojo.SearchResult;

/**
 * 商品搜索DAO
 * @author Administrator
 *
 */
@Repository
public class SearchDao {
	
	@Autowired 
	private SolrServer solrServer;
	/**
	 * 根据查询条件查询索引
	 * @param solrQuery
	 * @return
	 * @throws SolrServerException 
	 */
	 public SearchResult search(SolrQuery solrQuery) throws SolrServerException{
		 //根据query插叙索引
		 QueryResponse response = solrServer.query(solrQuery);
		 //封装查询结果,取高亮显示
		 SolrDocumentList documentList = response.getResults();
		 long numFound = documentList.getNumFound();
		 Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		 
		 SearchResult result = new SearchResult();
		 result.setRecordCount(numFound);
		 List<SearchItem> list = new ArrayList<>();
		 for (SolrDocument solrDocument : documentList) {
			SearchItem item = new SearchItem(); 
			item.setId((String)solrDocument.get("id"));
			item.setCategoryName((String)solrDocument.get("item_category_name"));
			item.setImage((String) solrDocument.get("item_image"));
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSellPoint((String) solrDocument.get("item_sell_point"));
			//取高亮显示
			List<String> highlinghtinglist = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (highlinghtinglist != null && highlinghtinglist.size() > 0 ) {
				title = highlinghtinglist.get(0);
			}else {
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			list.add(item);
		}
		 result.setItemList(list);
		 return result;
	 }

}
