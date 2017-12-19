package cn.lospark.activmq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMqTest {
	
	@Test
	public void QueueTest() throws Exception{
		
		//1、创建一个连接工厂对象，需要指定服务的ip及端口。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、使用工厂对象创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		//3、开启连接，调用Connection对象的start方法。
		connection.start();
		//4、创建一个Session对象。
		//第一个参数：是否开启事务。如果true开启事务，第二个参数无意义。一般不开启事务false。
		//第二个参数：应答模式。自动应答或者手动应答。一般自动应答。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、使用Session对象创建一个Destination对象。两种形式queue、topic，现在应该使用queue
		Queue queue = session.createQueue("test-01");
		//6、使用Session对象创建一个Producer对象。
		MessageProducer producer = session.createProducer(queue);
		//7、创建一个Message对象，可以使用TextMessage。
		TextMessage textMessage = session.createTextMessage("哈哈哈");
		/*TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello Activemq");*/
		//8、发送消息
		producer.send(textMessage);
		//9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		//创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象。queue对象
		Queue queue = session.createQueue("test-01");
		//使用Session对象创建一个消费者对象。
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMassage = (TextMessage) message;
				try {
					String text = textMassage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void TestTopicProducer() throws Exception{
		//1、创建一个连接工厂对象，需要指定服务的ip及端口。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、使用工厂对象创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		//3、开启连接，调用Connection对象的start方法。
		connection.start();
		//4、创建一个Session对象。
		//第一个参数：是否开启事务。如果true开启事务，第二个参数无意义。一般不开启事务false。
		//第二个参数：应答模式。自动应答或者手动应答。一般自动应答。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、使用Session对象创建一个Destination对象。两种形式queue、topic，现在应该使用queue
		Topic topic = session.createTopic("test-top");
		//6、使用Session对象创建一个Producer对象。
		MessageProducer producer = session.createProducer(topic);
		//7、创建一个Message对象，可以使用TextMessage。
		TextMessage textMessage = session.createTextMessage("topMessage");
		/*TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello Activemq");*/
		//8、发送消息
		producer.send(textMessage);
		//9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicConsumer() throws Exception{
		//创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象。
		Topic topic = session.createTopic("test-top");
		//使用Session对象创建一个消费者对象。
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMassage = (TextMessage) message;
				try {
					String text = textMassage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topic消费者2已经启动.......");
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
