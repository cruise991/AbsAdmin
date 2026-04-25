package com.abs.system.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Table;

@Table(name = "abs_logininfo")
public class AbsLoginInfo {

	private String rowguid;
	private LocalDateTime logindate;
	private String userinfo;
	private LocalDateTime outdate;
	private String usertoken;
	private String userguid;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public LocalDateTime getLogindate() {
		return logindate;
	}

	public void setLogindate(LocalDateTime logindate) {
		this.logindate = logindate;
	}

	public LocalDateTime getOutdate() {
		return outdate;
	}

	public void setOutdate(LocalDateTime outdate) {
		this.outdate = outdate;
	}

	public String getUsertoken() {
		return usertoken;
	}

	public void setUsertoken(String usertoken) {
		this.usertoken = usertoken;
	}

	public String getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(String userinfo) {
		this.userinfo = userinfo;
	}

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

}
