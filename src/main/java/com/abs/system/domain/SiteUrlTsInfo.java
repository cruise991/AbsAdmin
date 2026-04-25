package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;


@Table(name="site_baiduts")
public class SiteUrlTsInfo {

	private String rowguid;
	
	private String siteurl;

	private Date addtime;

	private Date tsdate;

	private String tsstatus;

	private String tscode;

	private String tsmsg;
	
	private String objectguid;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getSiteurl() {
		return siteurl;
	}

	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getTsdate() {
		return tsdate;
	}

	public void setTsdate(Date tsdate) {
		this.tsdate = tsdate;
	}

	public String getTsstatus() {
		return tsstatus;
	}

	public void setTsstatus(String tsstatus) {
		this.tsstatus = tsstatus;
	}

	public String getTscode() {
		return tscode;
	}

	public void setTscode(String tscode) {
		this.tscode = tscode;
	}

	public String getTsmsg() {
		return tsmsg;
	}

	public void setTsmsg(String tsmsg) {
		this.tsmsg = tsmsg;
	}



	public String getObjectguid() {
		return objectguid;
	}

	public void setObjectguid(String objectguid) {
		this.objectguid = objectguid;
	}

}
