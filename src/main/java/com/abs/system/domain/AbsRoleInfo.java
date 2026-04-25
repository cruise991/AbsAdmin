package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_roleinfo")
public class AbsRoleInfo {


	@Override
	public String toString() {
		return "RoleInfo [rowguid=" + rowguid + ", rolename=" + rolename + ", addtime=" + addtime + ", ordernum="
				+ ordernum + ", remark=" + remark + "]";
	}

	private String rowguid;

	private String rolename;

	private Date addtime;

	private int ordernum;

	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}
}
