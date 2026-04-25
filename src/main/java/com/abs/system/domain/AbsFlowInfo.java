package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;


@Table(name = "abs_flowinfo")
public class AbsFlowInfo {

	private String rowguid;
	private String flowname;
	private String userguid;
	private String description;
	private String status;
	private String startrole;
	private String startname;
	private String flowtype;
	private String typename;
	private String groupname;
	private String grouptype;

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

	private Date addtime;

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}

	public String getStartrole() {
		return startrole;
	}

	public void setStartrole(String startrole) {
		this.startrole = startrole;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFlowname() {
		return flowname;
	}

	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

}
