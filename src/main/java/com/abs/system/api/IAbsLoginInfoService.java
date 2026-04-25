package com.abs.system.api;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.domain.AbsLoginInfo;
import com.abs.system.util.Params;

@Service
public interface IAbsLoginInfoService {

    public void addLoginInfo(AbsLoginInfo loginInfo);
    
    public AbsLoginInfo getLoginInfoByToken(String usertoken);
    
    public void updateLogoutTime(String usertoken);
    
    public List<Map<String, Object>> getLoginLogList(Params params);
    
    public long getLoginLogCount(Params params);
    
    public List<Map<String, Object>> getLoginLogListByUser(String userinfo);
    
    public AbsLoginInfo getLoginInfoByRowguid(String rowguid);
    
    public void deleteLoginInfoByRowguid(String rowguid);
    
    public Map<String, Object> queryUserGuid(String token);
}