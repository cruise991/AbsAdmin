package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_flow_nconfig")
public class AbsFlowNconfig {

	private String rowguid;
	private String flowguid;
	private String nodeid;
	private String label;
	private String roleguid;
	private String page;
	private String action;
	private String remark;
	private Date addtime;
	
	
	

	
	public String getRoleguid() {
		return roleguid;
	}

	public void setRoleguid(String roleguid) {
		this.roleguid = roleguid;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getFlowguid() {
		return flowguid;
	}

	public void setFlowguid(String flowguid) {
		this.flowguid = flowguid;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
