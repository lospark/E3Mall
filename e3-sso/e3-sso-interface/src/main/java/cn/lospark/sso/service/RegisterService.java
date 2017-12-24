package cn.lospark.sso.service;

import cn.lospark.pojo.TbUser;
import cn.lospark.utils.E3Result;

/**
 * 用户登录功能
 * @author Administrator
 *
 */

public interface RegisterService {

	E3Result checkData(String param,int type);
	E3Result register(TbUser tbUser);

}
