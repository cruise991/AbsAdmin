package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;


@Table(name = "abs_sysupdate")
public class AbsSysUpdate {

	private String rowguid;
	private String remark;
	private String updatetype;
	private String bfpath;
	private int ishy;
	private Date addtime;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdatetype() {
		return updatetype;
	}

	public void setUpdatetype(String updatetype) {
		this.updatetype = updatetype;
	}

	public String getBfpath() {
		return bfpath;
	}

	public void setBfpath(String bfpath) {
		this.bfpath = bfpath;
	}

	public int getIshy() {
		return ishy;
	}

	public void setIshy(int ishy) {
		this.ishy = ishy;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

}
