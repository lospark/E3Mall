package cn.lospark.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	@Test
	public void testJedis() {
		//创建一个jedis对象（host、port）
		Jedis jedis = new Jedis("192.168.25.202", 6379);
		//直接使用jedis操作redis
		jedis.set("test123", "my first jedis");
		String string = jedis.get("test123");
		jedis.close();
		System.out.println(string); 
	}
	@Test
	public void testJedisPool() throws Exception {
		//创建一个连接池host、pool
		JedisPool jedisPool = new JedisPool("192.168.25.202",6379);
		//从连接池中获取一个连接jedis
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作redis
		String string = jedis.get("test123");
		System.out.println(string);
		jedis.close();
		//每次使用关闭连接，连接池才能回收资源
		jedisPool.close();
		
	}
	@Test
	public void testJedisCluster() throws Exception {
		
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.202", 7001));
		nodes.add(new HostAndPort("192.168.25.202", 7002));
		nodes.add(new HostAndPort("192.168.25.202", 7003));
		nodes.add(new HostAndPort("192.168.25.202", 7004));
		nodes.add(new HostAndPort("192.168.25.202", 7005));
		nodes.add(new HostAndPort("192.168.25.202", 7006));
		JedisCluster cluster = new JedisCluster(nodes);
		//cluster.set("clusterTest", "cluster123");
		String string = cluster.get("clusterTest");
		System.out.println(string);
		cluster.close();
	}

}
