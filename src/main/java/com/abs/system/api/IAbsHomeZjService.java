package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsHomeUser;
import com.abs.system.domain.AbsHomeZj;
import com.abs.system.util.Params;



public interface IAbsHomeZjService {

	boolean isExistSameCpath(String cpath, String rowguid);

	void addHomezj(AbsHomeZj homezj);

	List<Map<String, Object>> getHomezjPageList(Params params);

	long getHomezjCount(Params params);

	void delHomezjByGuid(String rowguid);

	 List<Map<String,Object>> queryHomeZjListByUserguid(String userguid);

	Map<String, Object> queryHOmezjMapByGuid(String rowguid);

	AbsHomeZj queryHomeZjInfoByGuid(String rowguid);

	void updateHomezj(AbsHomeZj homezj);

	void addHomezjUser(AbsHomeUser homeuser);

	boolean isExistSameHomeUser(String userguid, String zjguid);

	void delHomezjUserByUserGuidAndZjGuid(String userguid, String zjguid);

	String queryCpathByZjGuid(String czjguid);

}
