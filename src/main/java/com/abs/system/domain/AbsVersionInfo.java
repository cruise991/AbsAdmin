package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_versioninfo")
public class AbsVersionInfo {

	private String rowguid;

	private String version;

	private String description;

	private String people;

	private Date addtime;
	
	private String cliengguid;
	
	private String seeurl;
	

	public String getCliengguid() {
		return cliengguid;
	}

	public void setCliengguid(String cliengguid) {
		this.cliengguid = cliengguid;
	}

	public String getSeeurl() {
		return seeurl;
	}

	public void setSeeurl(String seeurl) {
		this.seeurl = seeurl;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

}
