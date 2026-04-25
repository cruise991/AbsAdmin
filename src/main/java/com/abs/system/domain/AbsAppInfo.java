package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;


@Table(name = "abs_appinfo")
public class AbsAppInfo {
	
	
	private String rowguid;
	private String appname;
	private String introduction;
	private String appkey;
	private String appsecret;
	private Date expiredtime;
	private String status;
	private Date addtime;
	private String userguid;
	private String token;
	private Date tokenexpired;
	private String tokentype;
	private int tokennum;

	
	
	
	
	
	
	
	
	
	
	
	public int getTokennum() {
		return tokennum;
	}
	public void setTokennum(int tokennum) {
		this.tokennum = tokennum;
	}
	public String getTokentype() {
		return tokentype;
	}
	public void setTokentype(String tokentype) {
		this.tokentype = tokentype;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getTokenexpired() {
		return tokenexpired;
	}
	public void setTokenexpired(Date tokenexpired) {
		this.tokenexpired = tokenexpired;
	}
	public String getRowguid() {
		return rowguid;
	}
	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getAppsecret() {
		return appsecret;
	}
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}
	public Date getExpiredtime() {
		return expiredtime;
	}
	public void setExpiredtime(Date expiredtime) {
		this.expiredtime = expiredtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
