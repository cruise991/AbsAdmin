package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_ouinfo")
public class AbsPicInfo {

	private String rowguid;
	private String userguid;
	private String picurl;
	private String remark;
	private String firstkey;
	private Date addtime;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFirstkey() {
		return firstkey;
	}

	public void setFirstkey(String firstkey) {
		this.firstkey = firstkey;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

}
