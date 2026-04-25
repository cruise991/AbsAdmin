package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;


@Table(name = "abs_home_zj")
public class AbsHomeZj {

	private String rowguid;
	private String cname;
	private String ordernum;
	private Date addtime;
	private String cpath;

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	
	

}
