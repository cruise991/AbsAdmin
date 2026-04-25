package com.abs.system.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Table;



@Table(name = "abs_flow_event")
public class AbsFlowEvent {

	private String rowguid;
	private String startuser;
	private String startname;
	private String groupname;
	private String pronodeid;
	private String pronodename;
	private String grouptype;
	private String flowtype;
	private int ordernum;
	private String flowname;
	private String flowguid;
	private String status;
	private LocalDateTime startdate;
	private LocalDateTime enddate;
	private LocalDateTime addtime;

	public String getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public String getPronodeid() {
		return pronodeid;
	}

	public void setPronodeid(String pronodeid) {
		this.pronodeid = pronodeid;
	}

	public String getPronodename() {
		return pronodename;
	}

	public void setPronodename(String pronodename) {
		this.pronodename = pronodename;
	}

	public String getStartname() {
		return startname;
	}

	public void setStartname(String startname) {
		this.startname = startname;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getStartuser() {
		return startuser;
	}

	public void setStartuser(String startuser) {
		this.startuser = startuser;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGrouptype() {
		return grouptype;
	}

	public void setGrouptype(String grouptype) {
		this.grouptype = grouptype;
	}

	public String getFlowname() {
		return flowname;
	}

	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}

	public String getFlowguid() {
		return flowguid;
	}

	public void setFlowguid(String flowguid) {
		this.flowguid = flowguid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getStartdate() {
		return startdate;
	}

	public void setStartdate(LocalDateTime startdate) {
		this.startdate = startdate;
	}

	public LocalDateTime getEnddate() {
		return enddate;
	}

	public void setEnddate(LocalDateTime enddate) {
		this.enddate = enddate;
	}

	public LocalDateTime getAddtime() {
		return addtime;
	}

	public void setAddtime(LocalDateTime addtime) {
		this.addtime = addtime;
	}

}
