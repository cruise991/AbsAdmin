package com.abs.system.domain;

import jakarta.persistence.Table;



@Table(name="abs_home_user")
public class AbsHomeUser {

	private String rowguid;
	private String czjguid;
	private String zjname;
	private String ordernumber;
	private String userguid;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getCzjguid() {
		return czjguid;
	}

	public void setCzjguid(String czjguid) {
		this.czjguid = czjguid;
	}

	public String getZjname() {
		return zjname;
	}

	public void setZjname(String zjname) {
		this.zjname = zjname;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

}
