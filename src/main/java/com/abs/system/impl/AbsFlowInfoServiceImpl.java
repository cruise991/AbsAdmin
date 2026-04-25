package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsFlowInfoService;
import com.abs.system.domain.AbsFlowEdge;
import com.abs.system.domain.AbsFlowEvent;
import com.abs.system.domain.AbsFlowInfo;
import com.abs.system.domain.AbsFlowNconfig;
import com.abs.system.domain.AbsFlowNode;
import com.abs.system.domain.AbsFlowOperate;
import com.abs.system.domain.AbsFlowProcess;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;


@Service
public class AbsFlowInfoServiceImpl implements IAbsFlowInfoService{

	@Override
	public void updateFlowInfo(AbsFlowInfo flow) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.updateEntity(flow, "rowguid");
	}

	@Override
	public void insertFlowInfo(AbsFlowInfo flow) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(flow);	
	}

	@Override
	public List<Map<String, Object>> queryFlowListByMap(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();

		return dbService.queryPageListMapById("flow.queryFlowListByMap", params);
	}

	@Override
	public long queryFlowCount(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.queryCountByById("flow.queryFlowCount",params);
	}

	@Override
	public void deleteFlowByGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.delByGuid(rowguid, AbsFlowInfo.class);
	}

	@Override
	public Map<String,Object> queryFlowMapByGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		return dbService.queryMapById("flow.queryFlowMapByGuid", params);
	}

	@Override
	public List<Map<String, Object>> getFlowListBygroupNum(String groupnum) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("groupnum", groupnum);
		return dbService.queryPageListMapById("flow.getFlowListBygroupNum", params);
		
	}

	@Override
	public void addFlowNode(AbsFlowNode fnode) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(fnode);
	}

	@Override
	public void addFlowEdge(AbsFlowEdge fedge) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(fedge);
	}

	@Override
	public List<Map<String, Object>> queryFlowNodeListByGuid(String flowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		return dbService.queryListForMapById("flow.queryFlowNodeListByGuid", params);
	}

	@Override
	public List<Map<String, Object>> queryFlowEdgeListByGuid(String flowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		return dbService.queryListForMapById("flow.queryFlowEdgeListByGuid", params);
	}

	@Override
	public void cleanNodeAndEdgeByGuid(String flowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		dbService.execteSqlById("flow.deleteNodeByGuid", params);
		dbService.execteSqlById("flow.deleteEdgeByGuid", params);
		
	}

	@Override
	public AbsFlowNconfig queryFlowNodeConfig(String flowguid, String nodeid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		params.put("nodeid", nodeid);
		return dbService.queryEntityById(AbsFlowNconfig.class,"flow.queryFlowNodeConfig", params);
	}

	
	
	@Override
	public Map<String, Object> queryFlowNodeMapConfig(String flowguid, String nodeid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		params.put("nodeid", nodeid);
		return dbService.queryMapById("flow.queryFlowNodeConfig", params);
	}

	@Override
	public void updateNodeConfig(AbsFlowNconfig nconfig) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.updateEntity(nconfig, "rowguid");
		
	}

	@Override
	public void addNodeConfig(AbsFlowNconfig nconfig) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(nconfig);
		
	}

	@Override
	public void updateFlowStatusByGuid(String rowguid, String status) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		params.put("status", status);
		dbService.execteSqlById("flow.updateFlowStatusByGuid", params);
		
	}

	@Override
	public AbsFlowInfo queryFlowInfoByGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		return dbService.queryEntityById(AbsFlowInfo.class, "flow.queryFlowInfoByGuid", params);
	}

	@Override
	public boolean isExistFlowConfig(String flowguid, String nodeid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		params.put("nodeid", nodeid);
	    long count=dbService.queryCountByById("flow.isExistFlowConfig",params);
	    if(count>0) {
	    	return true;
	    }else {
	    	return false;
	    }
	}

	@Override
	public AbsFlowNconfig queryFlowNodeConfigEntityConfig(String flowguid, String nodeid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		params.put("nodeid", nodeid);
	    return dbService.queryEntityById(AbsFlowNconfig.class,"flow.queryFlowNodeConfig",params);

	}

	@Override
	public AbsFlowNode queryFlowStartNodeByGuid(String flowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		return dbService.queryEntityById(AbsFlowNode.class,"flow.queryFlowStartNodeByGuid",params);
	}

	@Override
	public AbsFlowNode queryFlowNextNode(String flowguid, String currentNodeId) {
         
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("flowguid", flowguid);
		params.put("source", currentNodeId);
		Map<String,Object> map_target=dbService.queryMapById("flow.queryEdgeTargetByGuid",params);
        if(map_target!=null) {
        	String target=map_target.get("target")+"";
        	params.put("id", target);
        	return dbService.queryEntityById(AbsFlowNode.class,"flow.queryFlowNodeByGuidAndId",params);
        }else {
        	return null;
        }
	}

	@Override
	public void addFlowProcess(AbsFlowProcess pp) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(pp);
		
	}

	@Override
	public List<Map<String, Object>> queryFlowProcessList(String eventguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("eventguid", eventguid);
		return dbService.queryListForMapById("flow.queryFlowProcessList", params);
	}

	@Override
	public void addFlowEvent(AbsFlowEvent event) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(event);
		
	}

	@Override
	public AbsFlowEvent queryFlowEventByGuid(String eventguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", eventguid);
		return dbService.queryEntityById(AbsFlowEvent.class,"flow.queryFlowEventByGuid",params);
	}

	@Override
	public Map<String, Object> queryFlowCurrentStatusByGuid(String eventguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", eventguid);
		return dbService.queryMapById("flow.queryFlowCurrentStatusByGuid",params);
	}

	@Override
	public void addFlowOperate(AbsFlowOperate operate) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(operate);
		
	}

	@Override
	public Map<String, Object> queryFlowProcessOrderNum(String eventguid, String flowguid, String nextCodeId) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("eventguid", eventguid);
		params.put("flowguid", flowguid);
		params.put("nodeid", nextCodeId);
		return dbService.queryMapById("flow.queryFlowProcessOrderNum",params);
	}

	@Override
	public void updateFlowProcess(AbsFlowEvent event) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.updateEntity(event, "rowguid");
		
	}

	@Override
	public List<Map<String, Object>> getUserFlowEventList(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.queryPageListMapById("flow.getUserFlowEventList", params);
	}

	@Override
	public long queryUserFlowEventCount(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.queryCountByById("flow.queryUserFlowEventCount", params);
	}



}
