package cn.lospark.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.lospark.utils.FastDFSClient;

public class FastDfsTest {
	@Test
	public void testUpload() throws Exception {
		//创建一个配置文件。文件名任意。内容就是tracker服务器的地址。
		//使用全局对象加载配置文件。
		ClientGlobal.init("E:/IntelijIdeaProject/jee-eclipse/e3-manager-web/src/main/resources/conf/client.cnf");
		//创建一个TrackerClient对象
		TrackerClient client = new TrackerClient();
		//通过TrackClient获得一个TrackerServer对象
		TrackerServer trackerServer = client.getConnection();
		//创建一个StrorageServer的引用，可以是null
		StorageServer storageServer = null;
		//创建一个StorageClient，参数需要TrackerServer和StrorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用StorageClient上传文件。
		String[] upload_file = storageClient.upload_file("C:/Users/Administrator/Desktop/dianpubanner.png", "png", null);
		for (String string : upload_file) {
			System.out.println(string);
		}
	}
	@Test
	public void testFastDfsClient() throws Exception{
		FastDFSClient client = new FastDFSClient("E:/IntelijIdeaProject/jee-eclipse/e3-manager-web/src/main/resources/conf/client.cnf");
		String uploadFile = client.uploadFile("C:/Users/Administrator/Desktop/QQ截图20171122210820.png");
		System.err.println(uploadFile);
	}
}
