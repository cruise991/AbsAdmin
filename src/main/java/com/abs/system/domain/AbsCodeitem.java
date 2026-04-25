package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_codeitem")
public class AbsCodeitem {
	public String getRowguid() {
		return rowguid;
	}

	private String rowguid;

	private String itemorder;

	private String itemtext;

	private String itemname;

	private String itemvalue;

	private Date operatedate;

	private String operateguid;

	private String remark;

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getItemorder() {
		return itemorder;
	}

	public void setItemorder(String itemorder) {
		this.itemorder = itemorder;
	}

	public String getItemtext() {
		return itemtext;
	}

	public void setItemtext(String itemtext) {
		this.itemtext = itemtext;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemvalue() {
		return itemvalue;
	}

	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
	}

	public Date getOperatedate() {
		return operatedate;
	}

	public void setOperatedate(Date operatedate) {
		this.operatedate = operatedate;
	}

	public String getOperateguid() {
		return operateguid;
	}

	public void setOperateguid(String operateguid) {
		this.operateguid = operateguid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
