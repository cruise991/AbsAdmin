package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsVisiterInfo;
import com.abs.system.util.Params;

public interface IAbsVisiterInfoService {

	void addVisterInfo(AbsVisiterInfo visiter);

	List<Map<String, Object>> queryPageList(Params params);

	long queryTotalCount(Params params);

}
