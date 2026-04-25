package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_userzm")
public class AbsUserZm {

	private String rowguid;
	private String cliengguid;
	private Date addtime;
	private String userguid;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getCliengguid() {
		return cliengguid;
	}

	public void setCliengguid(String cliengguid) {
		this.cliengguid = cliengguid;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

}
