package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsTaskInfo;

public interface IAbsTaskInfoService {

	AbsTaskInfo getTaskInfoByGuid(String rowguid);

	List<Map<String,Object>>  getPageTaskInfo(Map<String, Object> map);

	long queryTaskCountBytaskName(String taskname);

	boolean isExistTaskInfo(String taskclass, String rowguid);

	void updateTaskInfo(AbsTaskInfo task);

	void insertTaskInfo(AbsTaskInfo task);

	void deleteTaskByGuid(String rowguid);

	List<AbsTaskInfo> getTaskList();

	List<Map<String, Object>> queryTaskListByMap(Map<String, Object> reqMap);

	Map<String,Object> queryTaskMapByGuid(String rowguid);

}
