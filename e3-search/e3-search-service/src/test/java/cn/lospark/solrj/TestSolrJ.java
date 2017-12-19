package cn.lospark.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	@Test
	public void addDoudument() throws Exception{
		//创建一个SolrServer对象，创建一个连接。参数solr服务的url
		String baseURL = "http://192.168.25.202:8080/solr/collection1"; 
		SolrServer solrServer = new HttpSolrServer(baseURL);
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域。文档中必须包含一个id域，所有的域的名称必须在schema.xml中定义。
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", "1000");
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	@Test
	public void query() throws SolrServerException{
		//创建一个SolrServer对象，创建一个连接。参数solr服务的url
		String baseURL = "http://192.168.25.202:8080/solr/collection1"; 
		SolrServer solrServer = new HttpSolrServer(baseURL);
		//创建一个查询对象
		SolrQuery solrQuery = new SolrQuery();
		//查询条件
		solrQuery.setQuery("手机");
		solrQuery.setStart(0);
		solrQuery.setRows(3);
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("<em>");
		//执行查询,获取所有信息
		QueryResponse response = solrServer.query(solrQuery);
		
		SolrDocumentList solrDocumentList = response.getResults();
		//总记录数
		long numFound = solrDocumentList.getNumFound();
		
		for (SolrDocument solrDocument : solrDocumentList) {
			Object object = solrDocument.get("id");
		}
		
	}

}
