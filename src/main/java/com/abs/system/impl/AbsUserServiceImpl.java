package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsUserService;
import com.abs.system.domain.AbsUserInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class AbsUserServiceImpl implements IAbsUserService {

	public boolean isExistUserName(String username) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("loginname", username);
		long count = dbService.queryCountByById("userinfo.isExistRealName", params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<Map<String, Object>> getUserListByLoginName(int pagenum, int pagesize, String loginname) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("pagenum", pagenum);
		params.put("pagesize", pagesize);
		params.put("loginname", loginname);
		return dbService.queryListForMapById("userinfo.getUserListByLoginName", params);
	}

	public long getTotalCountByLoginName(String loginname) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("loginname", loginname);
		return dbService.queryCountByById("userinfo.getTotalCountByLoginName", params);
	}

	public AbsUserInfo getUserByUsernameAndPassowrd(String username, String password) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("loginname", username);
		params.put("password", password);
		return dbService.queryEntityById(AbsUserInfo.class, "userinfo.getUserByUsernameAndPassowrd", params);
	}

	@Override
	public Map getUserMapByUsernameAndPassowrd(String username, String password) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("username", username);
		params.put("password", password);
		return dbService.queryMapById("userinfo.getUserMapByUsernameAndPassowrd", params);
	}
	
	

	@Override
	public Map getUserMapByEmail(String email) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("email", email);
		return dbService.queryMapById("userinfo.getUserMapByEmail", params);
	}

	
	@Override
	public Map getUserMapByEmailAndPassword(String email, String password) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("email", email);
		params.put("password", password);
		return dbService.queryMapById("userinfo.getUserMapByEmailAndPassword", params);
	}
	public boolean isExistEmail(String email) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("email", email);
        long count=dbService.queryCountByById("userinfo.isExistEmail", params);
        if(count>0) {
        	return true;
        }else {
        	return false;
        }
	}

	public AbsUserInfo getUserByEmail(String email) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("email", email);
		return dbService.queryEntityById(AbsUserInfo.class, "userinfo.getUserByEmail", params);
	}

	public long isUseingRole(String roleguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("userrole", roleguid);
		return dbService.queryCountByById("userinfo.isUseingRole", params);
	}

	public long isExistOuGuid(String ouguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("ouguid", ouguid);
		return dbService.queryCountByById("userinfo.isExistOuGuid", params);
	}

	@Override
	public AbsUserInfo getUserByPhone(String phone) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("phone", phone);
		return dbService.queryEntityById(AbsUserInfo.class, "userinfo.getUserByPhone", params);
	}

	@Override
	public AbsUserInfo getUserPhoneAndPassword(String phone, String md5) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("phone", phone);
		params.put("password", md5);
		return dbService.queryEntityById(AbsUserInfo.class, "userinfo.getUserPhoneAndPassword", params);
	}

	@Override
	public AbsUserInfo getUserByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.findOneEntityByGuid(rowguid, AbsUserInfo.class);
	}

	@Override
	public void updateUser(AbsUserInfo user) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.updateEntity(user, "rowguid");
	}

	@Override
	public Map<String, String> getUserNameByGuid(String userguid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUser(AbsUserInfo userinfo) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(userinfo);
	}

	@Override
	public List<AbsUserInfo> getListByCondition(String sqlcondition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUserLogoByGuid(String url, String userguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", userguid);
		params.put("url", url);
		dbService.execteSqlById("userinfo.updateUserLogoByGuid", params);
	}

	@Override
	public Map<String, Object> getUserRoleByGuid(String userguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", userguid);
		return dbService.queryMapById("userinfo.getUserRoleByGuid", params);
	}

	@Override
	public boolean isExistOpenid(String openid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("openid", openid);
		long count=dbService.queryCountByById("userinfo.isExistOpenid", params);
		if(count>0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public AbsUserInfo getUserInfoByOpenid(String openid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("openid", openid);
		return dbService.queryEntityById(AbsUserInfo.class,"userinfo.getUserInfoByOpenid", params);
	}

	@Override
	public Map queryUserMapByGuid(String userguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("userguid", userguid);
		return dbService.queryMapById("userinfo.queryUserMapByGuid", params);
	}




}
