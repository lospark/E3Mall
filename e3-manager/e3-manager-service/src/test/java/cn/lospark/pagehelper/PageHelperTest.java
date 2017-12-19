package cn.lospark.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.lospark.mapper.TbItemMapper;
import cn.lospark.pojo.TbItem;
import cn.lospark.pojo.TbItemExample;

public class PageHelperTest {
/*	@Test
	public void testPageHelper() throws Exception{
		//初始化Spring容器，获取Mapper代理对象的实现类
		ApplicationContext application =  new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper itemMapper = application.getBean(TbItemMapper.class);
		//执行sql语句之前设置分页信息，使用pagehelper的静态方法
		PageHelper.startPage(1, 10);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//获取分页信息，pageInfo 1.总记录数；2.总页数，当前页码
		 PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		 System.out.println(pageInfo.getTotal());
		 System.out.println(pageInfo.getPages());
		 System.out.println(list.size());
		
	}*/
}
