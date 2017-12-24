package cn.lospark.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.lospark.mapper.TbUserMapper;
import cn.lospark.pojo.TbUser;
import cn.lospark.pojo.TbUserExample;
import cn.lospark.pojo.TbUserExample.Criteria;
import cn.lospark.sso.service.RegisterService;
import cn.lospark.utils.E3Result;
/**
 * 用户注册处理
 * @author Administrator
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public E3Result checkData(String param, int type) {
		//根据不同type生成不同的查询条件1:用户名；2：手机号 3：邮箱
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		}else if (type == 2 ) {
			criteria.andPhoneEqualTo(param);
		}else if (type == 3) {
			criteria.andEmailEqualTo(param);
		}else{
			return E3Result.build(400, "数据类型错误");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//判断结果中是否存在数据
		if (list != null && list.size() > 0) {
			return E3Result.ok(false);
		}
		return E3Result.ok(true);
	}

	@Override
	public E3Result register(TbUser tbUser) {
		//数据有效性校验，username、password、phone不能为空
		if (StringUtils.isBlank(tbUser.getUsername()) || 
				StringUtils.isBlank(tbUser.getPassword()) ||
					StringUtils.isBlank(tbUser.getPhone())) {
			 return E3Result.build(400, "数据不完整!");
		}
		if (!(boolean)checkData(tbUser.getUsername(),1).getData()) {
			 return E3Result.build(400, "用户名重复");
		}
		if (!(boolean)checkData(tbUser.getPhone(), 2).getData()) {
			return E3Result.build(400, "手机号");
		}
		//补全属性
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		//password MD5 加密
		String password = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(password);
		
		userMapper.insert(tbUser);
		
		return E3Result.ok();
	}

}
