package com.abs.system.controller;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abs.system.api.IAbsFlowInfoService;
import com.abs.system.api.IAbsMsgInfoService;
import com.abs.system.api.IAbsUserService;
import com.abs.system.domain.AbsFlowEdge;
import com.abs.system.domain.AbsFlowEvent;
import com.abs.system.domain.AbsFlowInfo;
import com.abs.system.domain.AbsFlowNconfig;
import com.abs.system.domain.AbsFlowNode;
import com.abs.system.domain.AbsFlowProcess;
import com.abs.system.domain.AbsMsgInfo;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/flowinfo")
public class AbsFlowInfoController {

	private Logger logger = LoggerFactory.getLogger(AbsFlowInfoController.class);
	
	
	@Autowired
	private IAbsUserService userService;
	
	@Autowired
	private IAbsFlowInfoService flowService;
	
	
	@Autowired
	private IAbsMsgInfoService msgService;
	
	@ResponseBody
	@RequestMapping("saveflow")
	public JSONObject editconfig(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		
		logger.info("当前用户传入参数：{}",reqMap.toString());
		try {
			
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);			
			
			boolean isupdate = true;
			String rowguid=reqMap.get("rowguid")+"";
			String flowname=reqMap.get("flowname")+"";
			String startrole=reqMap.get("startrole")+"";
			String startname=reqMap.get("startname")+"";
			String typename=reqMap.get("typename")+"";
			String flowtype=reqMap.get("flowtype")+"";
			String grouptype=reqMap.get("grouptype")+"";
			String groupname=reqMap.get("groupname")+"";
			
			String description=reqMap.get("description")+"";
			
			AbsFlowInfo flow=new AbsFlowInfo();
			if (StrUtil.isStrBlank(rowguid)) {
				isupdate = false;
				flow.setRowguid(UUID.randomUUID().toString());
			}else {
				flow.setRowguid(rowguid);
			}
			flow.setUserguid(userguid);
			flow.setFlowname(flowname);
			flow.setStatus("0");
			flow.setAddtime(new Date());
			flow.setStartname(startname);
			flow.setStartrole(startrole);
			flow.setFlowtype(flowtype);
			flow.setTypename(typename);
			flow.setGroupname(groupname);
			flow.setGrouptype(grouptype);
			flow.setDescription(description);
			if (isupdate) {
				flowService.updateFlowInfo(flow);
			} else {
				flowService.insertFlowInfo(flow);
			}
			return BuildJsonOfObject.getJsonString("保存成功", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}
	

	@ResponseBody
	@RequestMapping("/getuserfloweventlist")
	public JSONObject getUserFlowEventList(@ToToken Params pp,@RequestBody Map<String, Object> reqMap) {
		try {
			String token=pp.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			
			Params params=new Params(reqMap);
			params.put("startuser", userguid);
			List<Map<String, Object>> list = flowService.getUserFlowEventList(params);
			if(list!=null) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(Map<String,Object> map:list) {
					if(!StrUtil.isObjBlank(map.get("startdate")))
					{
						Object startdateObj = map.get("startdate");
						Date date = null;
						if (startdateObj instanceof Date) {
							date = (Date) startdateObj;
						} else if (startdateObj instanceof LocalDateTime) {
							date = Date.from(((LocalDateTime) startdateObj).atZone(ZoneId.systemDefault()).toInstant());
						}
						if (date != null) {
							map.put("startdate", sdf.format(date));
						}
					}
					
				}
			}
			long count = flowService.queryUserFlowEventCount(params);
			return BuildJsonOfObject.getJsonString(list, count, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}

	@ResponseBody
	@RequestMapping("/getflowlist")
	public JSONObject getTasklist(@RequestBody Map<String, Object> reqMap) {
		try {
			Params params=new Params(reqMap);
			params.put("status", null);
			List<Map<String, Object>> list = flowService.queryFlowListByMap(params);
			if(list!=null) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				for(Map<String,Object> map:list) {
					if(!StrUtil.isObjBlank(map.get("addtime")))
					{
						Object addtimeObj = map.get("addtime");
						Date date = null;
						if (addtimeObj instanceof Date) {
							date = (Date) addtimeObj;
						} else if (addtimeObj instanceof LocalDateTime) {
							date = Date.from(((LocalDateTime) addtimeObj).atZone(ZoneId.systemDefault()).toInstant());
						}
						if (date != null) {
							map.put("addtime", sdf.format(date));
						}
					}
					
				}
			}
			long count = flowService.queryFlowCount(params);
			return BuildJsonOfObject.getJsonString(list, count, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	
	@ResponseBody
	@RequestMapping("/getflowlistofuser")
	public JSONObject getFlowListofuser(@RequestBody Map<String, Object> reqMap) {
		try {
			
			Params params=new Params(reqMap);
			params.put("status", "1");
			List<Map<String, Object>> list = flowService.queryFlowListByMap(params);
			if(list!=null) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				for(Map<String,Object> map:list) {
					if(!StrUtil.isObjBlank(map.get("addtime")))
					{
						map.put("addtime", sdf.format((Date)map.get("addtime")));	
					}
					
				}
			}
			long count = flowService.queryFlowCount(params);
			return BuildJsonOfObject.getJsonString(list, count, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	
	
	@ResponseBody
	@RequestMapping("/getflowlistbygroupnum")
	public JSONObject getflowlistbygroupnum(@RequestBody Map<String, Object> reqMap) {
		try {
			String groupnum=reqMap.get("groupnum").toString();
			List<Map<String, Object>> list = flowService.getFlowListBygroupNum(groupnum);
			JSONArray jsonarr=new JSONArray();
			//JSONArray jsonarr=new JSONArray();
			if(list!=null && list.size()>0) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				for(Map<String,Object> map:list) {
					JSONObject item=new JSONObject();
					if(!StrUtil.isObjBlank(map.get("otime")))
					{
						Object otimeObj = map.get("otime");
						Date date = null;
						if (otimeObj instanceof Date) {
							date = (Date) otimeObj;
						} else if (otimeObj instanceof LocalDateTime) {
							date = Date.from(((LocalDateTime) otimeObj).atZone(ZoneId.systemDefault()).toInstant());
						}
						if (date != null) {
							map.put("otime", sdf.format(date));
						}
					}else {
						map.put("otime", "~~~");
					}
					item.put("title", map.get("oname").toString());
					item.put("status",map.get("status").toString());	
					item.put("icon", "<SmileOutlined />");
					jsonarr.add(item);
					
				}
			}
			return BuildJsonOfObject.getJsonString(jsonarr,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}

	} 

	@ResponseBody
	@RequestMapping("/delflow")
	public JSONObject delFlow(@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguid = reqMap.get("rowguid").toString();
			flowService.deleteFlowByGuid(rowguid);
			return BuildJsonOfObject.getJsonString(MSG.delSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	

	@ResponseBody
	@RequestMapping("/getflowbyguid")
	public JSONObject getflowbyguid(@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguid = reqMap.get("rowguid").toString();
			Map<String,Object> map=flowService.queryFlowMapByGuid(rowguid);
			JSONObject jsonRtn=new JSONObject(map);
			return BuildJsonOfObject.getJsonString(jsonRtn,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/triggerflow")
	public JSONObject triggerflow(@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguid = reqMap.get("rowguid").toString();
			String status = reqMap.get("status").toString();
			flowService.updateFlowStatusByGuid(rowguid,status);
			return  BuildJsonOfObject.getJsonString("更新成功", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping("/saveflowdata")
	public JSONObject saveflowdata(@RequestBody Map<String, Object> reqMap) {
		logger.info("开始保存流程数据：{}",reqMap.toString());
		try {
			String data = reqMap.get("data").toString();
			String flowguid = reqMap.get("flowguid").toString();
			flowService.cleanNodeAndEdgeByGuid(flowguid);
			JSONObject jsonData=JSONObject.parseObject(data);
			JSONArray nodes =jsonData.getJSONArray("nodes");
			JSONArray edges =jsonData.getJSONArray("edges");
			if(nodes!=null && nodes.size()>0) {
				for(int i=0;i<nodes.size();i++) {
					JSONObject node=nodes.getJSONObject(i);
					String id=node.getString("id");
					String type=node.getString("type");
					String position=node.getString("position");
					String datas=node.getString("data");
					String measured=node.getString("measured");
					String selected=node.getString("selected");
					String dragging=node.getString("dragging");
					AbsFlowNode fnode=new AbsFlowNode();
					fnode.setFlowguid(flowguid);
					fnode.setRowguid(UUID.randomUUID().toString());
					fnode.setId(id);
					fnode.setType(type);
					fnode.setData(datas);
					fnode.setMeasured(measured);
					fnode.setDragging(dragging);
					fnode.setPosition(position);
					fnode.setSelected("false");
					flowService.addFlowNode(fnode);
				}	
			}
			if(edges!=null && edges.size()>0) {
				for(int i=0;i<edges.size();i++) {
					JSONObject edge=edges.getJSONObject(i);
					String id=edge.getString("id");
					String source=edge.getString("source");
					String sourceHandle=edge.getString("sourceHandle");
					String target=edge.getString("target");

					AbsFlowEdge fedge=new AbsFlowEdge();
					fedge.setRowguid(UUID.randomUUID().toString());
					fedge.setId(id);
					fedge.setSource(source);
					fedge.setSourcehandle(sourceHandle);
					fedge.setTarget(target);
					fedge.setFlowguid(flowguid);
					flowService.addFlowEdge(fedge);
				}
			}

			return BuildJsonOfObject.getJsonString("保存成功",MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/queryflowdata")
	public JSONObject queryFlowData(@RequestBody Map<String, Object> reqMap) {
		
		logger.info("开始获取节点配置信息：{}",reqMap.toString());
		try {
			String flowguid = reqMap.get("flowguid").toString();
			List<Map<String,Object>> list_node=flowService.queryFlowNodeListByGuid(flowguid);
			List<Map<String,Object>> list_edge=flowService.queryFlowEdgeListByGuid(flowguid);
			JSONObject jsonRtn=new JSONObject();
			JSONArray nodes=new JSONArray();
			JSONArray edges=new JSONArray();
			if(list_node!=null && list_node.size()>0) {
				for(Map<String,Object> nn:list_node) {
					JSONObject o=new JSONObject();
					o.put("id", nn.get("id"));
					o.put("type", nn.get("type"));
					o.put("position", JSONObject.parse(nn.get("position").toString()));
					o.put("data", JSONObject.parse(nn.get("data").toString()));
					o.put("measured", JSONObject.parse(nn.get("measured").toString()));
					//o.put("selected", nn.get("selected"));
					o.put("dragging", nn.get("dragging"));
					nodes.add(o);
				}
			}
			if(list_node!=null && list_node.size()>0) {	
				for(Map<String,Object> dd:list_edge) {
					JSONObject o=new JSONObject();
					o.put("source", dd.get("source"));
					o.put("sourceHandle", dd.get("sourceHandle"));
					o.put("target", dd.get("target"));
					o.put("data", dd.get("data"));
					o.put("id", dd.get("id"));
					edges.add(o);
				}
			}
			jsonRtn.put("nodes", nodes);
			jsonRtn.put("edges", edges);
			jsonRtn.put("code", MSG.SUCCESSCODE);
			logger.info(jsonRtn.toString());
			return jsonRtn;
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	
	
	@ResponseBody
	@RequestMapping("/querynodeconfig")
	public JSONObject queryNodeConfig(@RequestBody Map<String, Object> reqMap) {	
		logger.info("当前用户传入参数:{}",reqMap.toString());
		try {
			String flowguid = reqMap.get("flowguid").toString();
			String nodeid = reqMap.get("nodeid").toString();
			Map<String,Object> node_config=flowService.queryFlowNodeMapConfig(flowguid,nodeid);
			JSONObject jsonRtn=new JSONObject();
            if(node_config!=null) {
                logger.info("返回参数信息：{}",node_config.toString());
        		jsonRtn.put("data", node_config);
    			jsonRtn.put("code", MSG.SUCCESSCODE);
    			return jsonRtn;
            }else {
            	logger.info("未找到节点的配置");
        		jsonRtn.put("data","");
    			jsonRtn.put("code",MSG.FAILCODE);
    			return jsonRtn;
            }
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	
	
	@ResponseBody
	@RequestMapping("/savenodeconfig")
	public JSONObject saveNodeConfig(@RequestBody Map<String, Object> reqMap) {
		
		logger.info("开始保存用户数据：{}",reqMap.toString());
		try {
			String label = reqMap.get("label")+"";
			String roleguid = reqMap.get("roleguid")+"";
			String type = reqMap.get("type")+"";
			String page = reqMap.get("page")+"";
			String nodeid = reqMap.get("nodeid")+"";
			String flowguid = reqMap.get("flowguid")+"";
			String rowguid = reqMap.get("rowguid")+"";
			String remark = reqMap.get("remark")+"";
			String action = reqMap.get("action")+"";
			boolean isExist=flowService.isExistFlowConfig(flowguid,nodeid);
			AbsFlowNconfig nconfig=null;
			boolean isupdate=false;
			if(isExist) {
				isupdate=true;
				nconfig=flowService.queryFlowNodeConfigEntityConfig(flowguid, nodeid);
				
			}else {
				 nconfig=new AbsFlowNconfig();
				 nconfig.setRowguid(UUID.randomUUID().toString());
			}
			
			nconfig.setAddtime(new Date());
			nconfig.setAction(action);
			nconfig.setLabel(label);
			nconfig.setFlowguid(flowguid);
			nconfig.setNodeid(nodeid);
			nconfig.setPage(page);
			nconfig.setRemark(remark);
			nconfig.setRoleguid(roleguid);
			
			if(isupdate) {
				logger.info("更新...........");
				flowService.updateNodeConfig(nconfig);
			}else {
				flowService.addNodeConfig(nconfig);
			}
			return BuildJsonOfObject.getJsonString("保存成功", MSG.SUCCESSCODE);
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	

	
	
	@ResponseBody
	@RequestMapping("/startflow")
	public JSONObject startflow(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		logger.info("开始发起流程:{}",reqMap.toString());
		try {
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			String username=AbsSessionHelper.getCurrentUserName(token);
			List<AbsFlowProcess> list=new ArrayList<>();
			String flowguid = reqMap.get("flowguid").toString();
			
			
			AbsFlowInfo flowinfo=flowService.queryFlowInfoByGuid(flowguid);
			AbsFlowNode startNode=flowService.queryFlowStartNodeByGuid(flowguid);

			
			AbsFlowEvent event=new AbsFlowEvent();
			
			event.setRowguid(UUID.randomUUID().toString());
			event.setStartdate(LocalDateTime.now());
			event.setStartuser(userguid);
			event.setStartname(username);
			event.setFlowguid(flowinfo.getRowguid());
			event.setFlowname(flowinfo.getFlowname());
			event.setFlowtype(flowinfo.getFlowtype());

			event.setGrouptype(flowinfo.getGrouptype());
			event.setGroupname(flowinfo.getGroupname());
			event.setOrdernum(1);
			event.setStatus(MSG.Event_Init);
			event.setPronodeid(startNode.getId());
			JSONObject json=JSONObject.parseObject(startNode.getData());
			event.setPronodename(json.getString("label"));
			
			
			String nodeid=startNode.getId();
			int i=1;
			AbsFlowProcess process=new AbsFlowProcess();
			process.setFlowguid(flowguid);
			process.setIsend("0");
			process.setEventguid(event.getRowguid());
			process.setNodeguid(startNode.getRowguid());
			process.setNodeid(startNode.getId());
			JSONObject data=JSONObject.parseObject(startNode.getData());
			process.setNodename(data.getString("label"));
			process.setOrdernum(i);
			process.setRowguid(UUID.randomUUID().toString());
			list.add(process);
			AbsFlowNode nextNode=null;
			do {
				i++;
				nextNode=flowService.queryFlowNextNode(flowguid,nodeid);
				if(nextNode!=null) {
					nodeid=nextNode.getId();
					AbsFlowProcess process2=new AbsFlowProcess();
					process2.setFlowguid(flowguid);
					process2.setIsend("0");
					process2.setEventguid(event.getRowguid());
					process2.setNodeguid(nextNode.getRowguid());
					process2.setNodeid(nextNode.getId());
					JSONObject dataa=JSONObject.parseObject(nextNode.getData());
					process2.setNodename(dataa.getString("label"));
					process2.setOrdernum(i);
					process2.setRowguid(UUID.randomUUID().toString());
					list.add(process2);
				}
			}while(nextNode!=null);
            if(list!=null) {
            	flowService.addFlowEvent(event);
            	for(AbsFlowProcess pp:list) {
            		flowService.addFlowProcess(pp);
            	}
            }
    
		    return BuildJsonOfObject.getJsonString(event.getRowguid(), MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	
	@ResponseBody
	@RequestMapping("/queryflowstep")
	public JSONObject queryflowstep(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		logger.info("流程发起初始化:{}",reqMap.toString());
		try {
			String eventguid = reqMap.get("eventguid").toString();
            List<Map<String,Object>> flowlist=flowService.queryFlowProcessList(eventguid);
            if(flowlist!=null) {
            	for(Map<String,Object> mm:flowlist) {
            		mm.put("title", mm.get("nodename"));
            	}
            }
            Map<String,Object> map=flowService.queryFlowCurrentStatusByGuid(eventguid);
            String status=map.get("status")+"";
            String ordernum=map.get("ordernum")+"";
            JSONObject jsonRtn=new JSONObject();
            jsonRtn.put("flowlist", flowlist);
            jsonRtn.put("code",  MSG.SUCCESSCODE);
            jsonRtn.put("status", status);
            jsonRtn.put("ordernum", Integer.parseInt(ordernum)-1);
            logger.info("结束初始化结束");
		    return jsonRtn;
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	
	@ResponseBody
	@RequestMapping("/nextstep")
	public JSONObject nextstep(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		logger.info("开始执行:{}",reqMap.toString());
		try {

			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			
			
			String eventguid = reqMap.get("eventguid").toString();

			AbsFlowEvent event=flowService.queryFlowEventByGuid(eventguid);
			String cnodeid=event.getPronodeid();
			String flowguid=event.getFlowguid();
			
			AbsFlowNconfig config=flowService.queryFlowNodeConfig(flowguid, cnodeid);
			
			if("JSCL".contentEquals(event.getFlowtype())) {
				String action=config.getAction();
				int len=action.lastIndexOf(".");
				
				String clazzName=action.substring(0, len);
				String methodName=action.substring(len+1, action.length());
				Class<?> clazz = Class.forName(clazzName);
				Method method=clazz.getMethod(methodName);
				Object instance = clazz.newInstance();
				method.invoke(instance);
			}else {
				String roleguid=config.getRoleguid();
				//插入消息，相关人员
				
				AbsMsgInfo msginfo=new AbsMsgInfo();
				msginfo.setRowguid(UUID.randomUUID().toString());
				msginfo.setAddtime(new Date());
				msginfo.setIsread("0");
				msginfo.setMsgtitle("【流程处理】");
				msginfo.setRoleguid(roleguid);
				msginfo.setMsgtype("flow");
				msginfo.setContent(event.getStartname()+event.getFlowname());
				msginfo.setDatasource("1");
				msginfo.setEventguid(eventguid);
				msgService.addMsgInfo(msginfo);
			}
	
				
			AbsFlowNode nextNode=flowService.queryFlowNextNode(flowguid, cnodeid);
			
			if(nextNode==null) {
				event.setStatus(MSG.Event_End);				
				flowService.updateFlowProcess(event);
			}else {
				String nextCodeId=nextNode.getId();
				JSONObject json= JSONObject.parseObject(nextNode.getData());
				String nextLabel=json.getString("label");
				
				event.setPronodeid(nextCodeId);
				event.setPronodename(nextLabel);
				event.setStatus(MSG.Event_Doing);
				
				Map<String,Object> mm=flowService.queryFlowProcessOrderNum(eventguid,flowguid,nextCodeId);
				String ordernum=mm.get("ordernum")+"";
				event.setOrdernum(Integer.parseInt(ordernum));
				flowService.updateFlowProcess(event);
			}
		    return BuildJsonOfObject.getJsonString("成功", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	
	@ResponseBody
	@RequestMapping("/allstep")
	public JSONObject allstep(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		logger.info("开始执行:{}",reqMap.toString());
		try {

			String eventguid = reqMap.get("eventguid").toString();
			AbsFlowNode nextNode=null;
			do{
				AbsFlowEvent event=flowService.queryFlowEventByGuid(eventguid);
				String cnodeid=event.getPronodeid();
				String flowguid=event.getFlowguid();
				AbsFlowNconfig config=flowService.queryFlowNodeConfig(flowguid, cnodeid);
				String action=config.getAction();
				int len=action.lastIndexOf(".");
				
				String clazzName=action.substring(0, len);
			
				String methodName=action.substring(len+1, action.length());
				Class<?> clazz = Class.forName(clazzName);
				Method method=clazz.getMethod(methodName);
				Object instance = clazz.newInstance();
				method.invoke(instance);
					
				nextNode=flowService.queryFlowNextNode(flowguid, cnodeid);
				
				if(nextNode==null) {
					event.setStatus(MSG.Event_End);				
					flowService.updateFlowProcess(event);
				}else {
					String nextCodeId=nextNode.getId();
					JSONObject json= JSONObject.parseObject(nextNode.getData());
					String nextLabel=json.getString("label");
					
					event.setPronodeid(nextCodeId);
					event.setPronodename(nextLabel);
					event.setStatus(MSG.Event_Doing);
					
					Map<String,Object> mm=flowService.queryFlowProcessOrderNum(eventguid,flowguid,nextCodeId);
					String ordernum=mm.get("ordernum")+"";
					event.setOrdernum(Integer.parseInt(ordernum));
					flowService.updateFlowProcess(event);
				}
			}while(nextNode!=null);
		    return BuildJsonOfObject.getJsonString("成功", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}

	}
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping("/querypageurl")
	public JSONObject queryPageUrl(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		logger.info("开始查询页面配置执行地址:{}",reqMap.toString());
		try {
			String eventguid = reqMap.get("eventguid").toString();
			AbsFlowEvent event=flowService.queryFlowEventByGuid(eventguid);
			String cnodeid=event.getPronodeid();
			String flowguid=event.getFlowguid();
			AbsFlowNconfig config=flowService.queryFlowNodeConfig(flowguid, cnodeid);
			String page=config.getPage();
		    return BuildJsonOfObject.getJsonString(page, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}
	}
	
	@ResponseBody
	@RequestMapping("/isusercanoperate")
	public JSONObject isUserCanOperate(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		logger.info("判断当前事项是否可以操作:{}",reqMap.toString());
		try {
			
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			String eventguid=reqMap.get("eventguid").toString();
			AbsFlowEvent flowEvent=flowService.queryFlowEventByGuid(eventguid);
			String cnodeid=flowEvent.getPronodeid();
			String flowguid=flowEvent.getFlowguid();
			AbsFlowNconfig config=flowService.queryFlowNodeConfig(flowguid, cnodeid);
			String roleguid=config.getRoleguid();
			Map<String,Object> map=userService.getUserRoleByGuid(userguid);
			int iscan=0;
			if(map!=null) {
				String userole=map.get("userrole").toString();
				if(userole.contentEquals(roleguid)) {
					iscan=1;
				}
			}
		    return BuildJsonOfObject.getJsonString(iscan+"", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.SUCCESSCODE);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
