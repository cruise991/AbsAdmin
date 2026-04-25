package com.abs.system.api;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.domain.AbsUserInfo;

@Service
public interface IAbsUserService {

	public boolean isExistUserName(String username);
	
	
	public boolean isExistOpenid(String openid);

	public AbsUserInfo getUserByGuid(String rowguid);

	public void updateUser(AbsUserInfo user);

	public AbsUserInfo getUserByUsernameAndPassowrd(String username, String password);

	public boolean isExistEmail(String email);

	public AbsUserInfo getUserByEmail(String email);

	public long isUseingRole(String roleguid);

	public long isExistOuGuid(String rowguid);

	public List<AbsUserInfo> getListByCondition(String sqlcondition);

	public AbsUserInfo getUserByPhone(String shopphone);

	public AbsUserInfo getUserPhoneAndPassword(String username, String md5);

	public Map<String, String> getUserNameByGuid(String userguid);

	public void addUser(AbsUserInfo userinfo);

	public List<Map<String, Object>> getUserListByLoginName(int pagenum, int pagesize, String loginname);

	public long getTotalCountByLoginName(String loginname);

	public Map getUserMapByUsernameAndPassowrd(String username, String md5);

	public void updateUserLogoByGuid(String url, String userguid);

	public Map<String, Object> getUserRoleByGuid(String userguid);

	public Map getUserMapByEmailAndPassword(String email, String passowrd);

	public Map getUserMapByEmail(String email);

	public AbsUserInfo getUserInfoByOpenid(String openid);

	public Map queryUserMapByGuid(String userguid);





}
