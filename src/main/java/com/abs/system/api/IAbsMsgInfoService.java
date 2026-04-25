package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsMsgInfo;

public interface IAbsMsgInfoService {

	void addMsgInfo(AbsMsgInfo msginfo);

	List<Map<String, Object>> queryMsgListByUserGuidOfAdmin(Map<String,Object> reqMap);

	long queryCountByUserGuidOfAdmin(Map<String, Object> reqMap);

	void updateMsgReadStatus(String rowguid, String isread);

}
