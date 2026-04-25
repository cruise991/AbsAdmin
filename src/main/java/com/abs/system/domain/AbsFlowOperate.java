package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_flow_operate")
public class AbsFlowOperate {

	private String rowguid;
	private String eventguid;
	private String userguid;
	private String username;
	private Date operatedate;
	private String operate;
	private String operateid;
	private String nodeid;

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public Date getOperatedate() {
		return operatedate;
	}

	public void setOperatedate(Date operatedate) {
		this.operatedate = operatedate;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getEventguid() {
		return eventguid;
	}

	public void setEventguid(String eventguid) {
		this.eventguid = eventguid;
	}

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getOperateid() {
		return operateid;
	}

	public void setOperateid(String operateid) {
		this.operateid = operateid;
	}

}
