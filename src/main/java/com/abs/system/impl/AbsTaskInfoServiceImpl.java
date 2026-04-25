package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsTaskInfoService;
import com.abs.system.domain.AbsTaskInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;



@Service
public class AbsTaskInfoServiceImpl implements IAbsTaskInfoService{

	@Override
	public AbsTaskInfo getTaskInfoByGuid(String rowguid) {
	    AbsDbService dbService=AbsDbHelper.getDbService();
	    return dbService.findOneEntityByGuid(rowguid, AbsTaskInfo.class);
	}

	@Override
	public List<Map<String,Object>> getPageTaskInfo(Map<String, Object> map) {
		  AbsDbService dbService=AbsDbHelper.getDbService();
		  Params params=new Params(map);
		  return dbService.queryPageListMapById("taskinfo.getPageTaskInfoList", params);
	}
	
	
	@Override
	public long queryTaskCountBytaskName(String taskname) {
		  AbsDbService dbService=AbsDbHelper.getDbService();
		  Params params=new Params();
		  params.put("taskname", taskname);
		  return dbService.queryCountByById("taskinfo.queryTaskCountBytaskName", params);
	}

	@Override
	public boolean isExistTaskInfo(String taskclass, String rowguid) {
		  AbsDbService dbService=AbsDbHelper.getDbService();
		  Params params=new Params();
		  params.put("taskclass", taskclass);
		  params.put("rowguid", rowguid);
		  long count=dbService.queryCountByById("taskinfo.isExistTaskInfo", params);
		  if(count>0) {
			  return true;
		  }else {
			  return false;
		  }
	}

	@Override
	public void updateTaskInfo(AbsTaskInfo task) {
		  AbsDbService dbService=AbsDbHelper.getDbService();
		  dbService.updateEntity(task, "rowguid");
		
	}

	@Override
	public void insertTaskInfo(AbsTaskInfo task) {
		  AbsDbService dbService=AbsDbHelper.getDbService();
		  dbService.addEntity(task);
	}

	@Override
	public void deleteTaskByGuid(String rowguid) {
		 AbsDbService dbService=AbsDbHelper.getDbService();
		 dbService.delByGuid(rowguid, AbsTaskInfo.class);
	}

	@Override
	public List<AbsTaskInfo> getTaskList() {
		 AbsDbService dbService=AbsDbHelper.getDbService();
		 String sql="select * from abs_taskinfo";
		 return dbService.queryListForEntityBySql(AbsTaskInfo.class,sql);
	}

	@Override
	public List<Map<String, Object>> queryTaskListByMap(Map<String, Object> reqMap) {
		 AbsDbService dbService=AbsDbHelper.getDbService();
		 Params params=new Params(reqMap);
		 return dbService.queryPageListMapById("taskinfo.getPageTaskInfoList",params);
	}

	@Override
	public Map<String, Object> queryTaskMapByGuid(String rowguid) {
		 AbsDbService dbService=AbsDbHelper.getDbService();
		 Params params=new Params();
		 params.put("rowguid",rowguid);
		 return dbService.queryMapById("taskinfo.queryTaskMapByGuid",params);
	}

}
