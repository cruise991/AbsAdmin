package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsLoginInfoService;
import com.abs.system.domain.AbsLoginInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class AbsLoginInfoImpl implements IAbsLoginInfoService {

    @Override
    public void addLoginInfo(AbsLoginInfo loginInfo) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        dbService.addEntity(loginInfo);
    }

    @Override
    public AbsLoginInfo getLoginInfoByToken(String usertoken) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        Params params = new Params();
        params.put("usertoken", usertoken);
        return dbService.queryEntityById(AbsLoginInfo.class, "logininfo.queryUserGuid", params);
    }

    @Override
    public void updateLogoutTime(String usertoken) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        Params params = new Params();
        params.put("usertoken", usertoken);
        dbService.execteSqlById("logininfo.loginOut", params);
    }

    @Override
    public List<Map<String, Object>> getLoginLogList(Params params) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        return dbService.queryListForMapById("logininfo.getLoginLogList", params);
    }

    @Override
    public long getLoginLogCount(Params params) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        return dbService.queryCountByById("logininfo.getLoginLogCount", params);
    }

    @Override
    public List<Map<String, Object>> getLoginLogListByUser(String userinfo) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        Params params = new Params();
        params.put("userinfo", userinfo);
        return dbService.queryListForMapById("logininfo.getLoginLogListByUser", params);
    }

    @Override
    public AbsLoginInfo getLoginInfoByRowguid(String rowguid) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        Params params = new Params();
        params.put("rowguid", rowguid);
        return dbService.queryEntityById(AbsLoginInfo.class, "logininfo.getLoginInfoByRowguid", params);
    }

    @Override
    public void deleteLoginInfoByRowguid(String rowguid) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        Params params = new Params();
        params.put("rowguid", rowguid);
        dbService.execteSqlById("logininfo.deleteLoginInfoByRowguid", params);
    }

    @Override
    public Map<String, Object> queryUserGuid(String token) {
        AbsDbService dbService = AbsDbHelper.getDbService();
        Params params = new Params();
        params.put("usertoken", token);
        return dbService.queryMapById("logininfo.queryUserGuid", params);
    }
}