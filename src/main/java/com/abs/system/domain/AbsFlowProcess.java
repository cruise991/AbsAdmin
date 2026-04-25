package com.abs.system.domain;

import jakarta.persistence.Table;



@Table(name = "abs_flow_process")
public class AbsFlowProcess {

	private String rowguid;
	private String eventguid;
	private String flowguid;
	private String nodeguid;
	private String nodename;
	private String nodeid;
	private String isend;
	private int ordernum;
	
	
	

	public String getEventguid() {
		return eventguid;
	}

	public void setEventguid(String eventguid) {
		this.eventguid = eventguid;
	}

	public String getNodeguid() {
		return nodeguid;
	}

	public void setNodeguid(String nodeguid) {
		this.nodeguid = nodeguid;
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

	public String getNodename() {
		return nodename;
	}

	public void setNodename(String nodename) {
		this.nodename = nodename;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getIsend() {
		return isend;
	}

	public void setIsend(String isend) {
		this.isend = isend;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

}
