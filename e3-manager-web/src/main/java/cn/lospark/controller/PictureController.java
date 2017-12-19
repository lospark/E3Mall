package cn.lospark.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lospark.utils.FastDFSClient;
import cn.lospark.utils.JsonUtils;

/**
 * 图片上传的controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/pic")
public class PictureController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String baseUrl;
	
	@RequestMapping(value="/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile)  {
		Map<String,Object> result = new HashMap<>();
		try {
			//把图片上传到图片服务器
			FastDFSClient client = new FastDFSClient("classpath:conf/client.cnf");
			//取文件字节数组和扩展名
			String fileName = uploadFile.getOriginalFilename();
			String extName = fileName.substring(fileName.lastIndexOf(".")+1);
			String imageUrl = client.uploadFile(uploadFile.getBytes(), extName);
			//得到图片的访问地址和文件名（补充成完整的url）
			imageUrl = baseUrl + imageUrl;
			//封装到map中返回
			result.put("error", 0);
			result.put("url", imageUrl);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", 1);
			result.put("url", "上传图片失败！");
		}
		return JsonUtils.objectToJson(result);
	}

}
