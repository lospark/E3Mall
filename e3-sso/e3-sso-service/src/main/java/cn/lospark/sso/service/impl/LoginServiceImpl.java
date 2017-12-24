package cn.lospark.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.lospark.jedis.JedisClient;
import cn.lospark.mapper.TbUserMapper;
import cn.lospark.pojo.TbUser;
import cn.lospark.pojo.TbUserExample;
import cn.lospark.pojo.TbUserExample.Criteria;
import cn.lospark.sso.service.LoginService;
import cn.lospark.utils.E3Result;
import cn.lospark.utils.JsonUtils;

/**
 *用户登录处理 
 */
@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient JedisClient;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	
	@Override
	public E3Result userLogin(String userName, String password) {
		/*
		 * 1.判断用户名和密码是否准确（不正确，登录失败）
		 * 2.生成token（UUID）
		 * 3.将用户信息写入Redis：key：token，value：用户信息
		 * 4.设置session的过期时间
		 * 5.将session写入到cookie，但是需要在表现层处理
		 */
		//1.判断用户名和密码是否准确（不正确，登录失败）
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(userName);
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return E3Result.build(400, "用户或密码错误");
		}
		TbUser user = list.get(0);
		if (!StringUtils.equals(user.getPassword(),
				DigestUtils.md5DigestAsHex(password.getBytes()))) {
			return E3Result.build(400, "用户或密码错误");
		}
		//2.生成token（UUID）
		String token = UUID.randomUUID().toString();
		//3.将用户信息写入Redis：key：token，value：用户信息
		user.setPassword(null);
		JedisClient.set("SESSION:"+token,JsonUtils.objectToJson(user));
		//4.设置session的过期时间
		JedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		
		return E3Result.ok(token);
	}

}
