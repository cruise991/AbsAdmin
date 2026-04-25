package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsFlowEdge;
import com.abs.system.domain.AbsFlowEvent;
import com.abs.system.domain.AbsFlowInfo;
import com.abs.system.domain.AbsFlowNconfig;
import com.abs.system.domain.AbsFlowNode;
import com.abs.system.domain.AbsFlowOperate;
import com.abs.system.domain.AbsFlowProcess;
import com.abs.system.util.Params;

public interface IAbsFlowInfoService {

	void updateFlowInfo(AbsFlowInfo flow);

	void insertFlowInfo(AbsFlowInfo flow);

	List<Map<String, Object>> queryFlowListByMap(Params params);

	long queryFlowCount(Params params);

	void deleteFlowByGuid(String rowguid);

	Map queryFlowMapByGuid(String rowguid);

	List<Map<String, Object>> getFlowListBygroupNum(String groupnum);

	void addFlowNode(AbsFlowNode fnode);

	void addFlowEdge(AbsFlowEdge fedge);

	List<Map<String, Object>> queryFlowNodeListByGuid(String flowguid);

	List<Map<String, Object>> queryFlowEdgeListByGuid(String flowguid);

	void cleanNodeAndEdgeByGuid(String flowguid);

	AbsFlowNconfig queryFlowNodeConfig(String flowguid, String nodeid);

	void updateNodeConfig(AbsFlowNconfig nconfig);

	void addNodeConfig(AbsFlowNconfig nconfig);

	void updateFlowStatusByGuid(String rowguid, String status);

	AbsFlowInfo queryFlowInfoByGuid(String rowguid);

	boolean isExistFlowConfig(String flowguid, String nodeid);

	AbsFlowNconfig queryFlowNodeConfigEntityConfig(String flowguid, String nodeid);

	AbsFlowNode queryFlowStartNodeByGuid(String rowguid);

	AbsFlowNode queryFlowNextNode(String flowguid, String currentNodeId);

	void addFlowProcess(AbsFlowProcess pp);

	List<Map<String, Object>> queryFlowProcessList(String flowguid);

	void addFlowEvent(AbsFlowEvent event);

	AbsFlowEvent queryFlowEventByGuid(String eventguid);

	Map<String, Object> queryFlowCurrentStatusByGuid(String eventguid);

	void addFlowOperate(AbsFlowOperate operate);

	Map<String, Object> queryFlowNodeMapConfig(String flowguid, String nodeid);

	Map<String, Object> queryFlowProcessOrderNum(String eventguid, String flowguid, String nextCodeId);

	void updateFlowProcess(AbsFlowEvent event);

	List<Map<String, Object>> getUserFlowEventList(Params params);

	long queryUserFlowEventCount(Params params);

}
