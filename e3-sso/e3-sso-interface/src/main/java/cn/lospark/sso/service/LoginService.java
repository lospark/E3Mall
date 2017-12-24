package cn.lospark.sso.service;

import cn.lospark.utils.E3Result;

public interface LoginService {
	//参数：用户名，密码
	//返回值：
	//业务逻辑
	/*
	 * 1.判断用户名和密码是否准确（不正确，登录失败）
	 * 2.生成token（UUID）
	 * 3.将用户信息写入Redis：key：token，value：用户信息
	 * 4.设置session的过期时间
	 * 5.将session写入到cookie，但是需要在表现层处理
	 */
	E3Result userLogin(String userName,String password);
}
