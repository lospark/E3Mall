package cn.lospark.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lospark.common.pojo.SearchItem;
import cn.lospark.search.mapper.ItemMapper;
import cn.lospark.search.service.SearchItemService;
import cn.lospark.utils.E3Result;
/**
 * 索引库维护service
 * @author Administrator
 *
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired 
	private SolrServer solrServer;

	@Override
	public E3Result importAllItems() {
		try {
			//1.查询商品列表
			List<SearchItem> itemList = itemMapper.getItemList();
			//2.遍历商品列表
			for (SearchItem searchItem : itemList) {
				//3.创建文档对象
				SolrInputDocument document = new  SolrInputDocument();
				//4.向文档对象中添加域
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_sell_point", searchItem.getSellPoint());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategoryName());
				//5.把文档对象写入索引库
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
			return E3Result.ok();
		} catch (Exception e) {
			return E3Result.build(104, "数据导入时发生异常");
		}
	}

}
