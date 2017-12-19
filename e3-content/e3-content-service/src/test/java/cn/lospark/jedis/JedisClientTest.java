package cn.lospark.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisClientTest {
	
	@Test
	public void testJedisClient() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//通过接口获取实例化对象
		JedisClient client = applicationContext.getBean(JedisClient.class);
		/*client.set("jedisClientTest", "jedisClientTest123");
		System.out.println(client.get("jedisClientTest"));*/
		client.set("jedisCluserTest", "jedisCluserTest123");
		System.out.println(client.get("jedisCluserTest"));
		
	}

}
