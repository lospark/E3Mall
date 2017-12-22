package cn.lospark.item.messagelistener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.lospark.item.pojo.Item;
import cn.lospark.pojo.TbItem;
import cn.lospark.pojo.TbItemDesc;
import cn.lospark.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 监听商品添加消息，生成对应的静态页面
 * @author Administrator
 *
 */
public class HtmlGenatatorListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	
	@Override
	public void onMessage(Message messager) {
		//1.创建模板，参考jsp
		try {
			//2.从消息中取商品ID
			TextMessage textMessage = (TextMessage) messager;
			String text = textMessage.getText();
			long itemId = Long.valueOf(text);
			/**等待事务提交**/
			Thread.sleep(1000);
			
			//3.根据商品Id查询商品信息，商品基本信息和商品描述
			TbItem ibItem = itemService.getItemById(itemId);
			Item item = new Item(ibItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			//4.创建一个数据集（Map对象），把商品数据封装
			Map<String,Object> data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			//5.加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//6.创建输出流
			Writer out = new FileWriter(new File(HTML_GEN_PATH + itemId +".html"));
			//7.生成静态页面。
			template.process(data, out);
			//8.关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
