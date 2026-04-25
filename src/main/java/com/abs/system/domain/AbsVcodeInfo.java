package com.abs.system.domain;

import jakarta.persistence.Table;



@Table(name = "abs_vcode")
public class AbsVcodeInfo {

	private String rowguid;
	private String email;
	private String vcode;
	private int sendstatus;
	private long expiredtime;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public int getSendstatus() {
		return sendstatus;
	}

	public void setSendstatus(int sendstatus) {
		this.sendstatus = sendstatus;
	}

	public long getExpiredtime() {
		return expiredtime;
	}

	public void setExpiredtime(long expiredtime) {
		this.expiredtime = expiredtime;
	}
    

}
