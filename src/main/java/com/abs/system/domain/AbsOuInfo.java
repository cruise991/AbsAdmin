package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;

@Table(name = "abs_ouinfo")
public class AbsOuInfo {

	private String rowguid;
	private String ouname;
	private String oucode;
	private String ouaddresstel;
	private String oubankaccount;
	private String remark;
	private Date addtime;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getOuname() {
		return ouname;
	}

	public void setOuname(String ouname) {
		this.ouname = ouname;
	}

	public String getOucode() {
		return oucode;
	}

	public void setOucode(String oucode) {
		this.oucode = oucode;
	}

	public String getOuaddresstel() {
		return ouaddresstel;
	}

	public void setOuaddresstel(String ouaddresstel) {
		this.ouaddresstel = ouaddresstel;
	}

	public String getOubankaccount() {
		return oubankaccount;
	}

	public void setOubankaccount(String oubankaccount) {
		this.oubankaccount = oubankaccount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

}
