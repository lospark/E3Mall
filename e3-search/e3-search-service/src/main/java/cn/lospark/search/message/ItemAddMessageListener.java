package cn.lospark.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.lospark.common.pojo.SearchItem;
import cn.lospark.search.mapper.ItemMapper;
/**
 * 监听商品添加消息，接受消息后，将对应的商品的信息同步到索引库
 * @author Administrator
 *
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		try {
			//1从消息中取出id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			long itemId = Long.parseLong(text);
			//等待事务提交
			Thread.sleep(1000);
			//2.根据商品id查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			//3.创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			//4.向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_sell_point", searchItem.getSellPoint());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategoryName());
			//5.把文档写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}