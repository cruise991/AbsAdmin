package com.abs.system.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Table;


@Table(name = "abs_userinfo")
public class AbsUserInfo {

	private String rowguid;
	private String loginname;
	private String password;
	private String phone;
	private String email;
	private String address;
	private String logourl;
	private String userrole;
	private String isdelete;

	private String realname;
	private String openid;
	private String ouguid;
	private String remark;
	private LocalDateTime addtime;

	public String getOuguid() {
		return ouguid;
	}

	public void setOuguid(String ouguid) {
		this.ouguid = ouguid;
	}

	// 用户角

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	public LocalDateTime getAddtime() {
		return addtime;
	}

	public void setAddtime(LocalDateTime addtime) {
		this.addtime = addtime;
	}

	public String getRemark() {
		return remark;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
