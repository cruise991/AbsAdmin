package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_visiterinfo")
public class AbsVisiterInfo {

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getVisiterip() {
		return visiterip;
	}

	public void setVisiterip(String visiterip) {
		this.visiterip = visiterip;
	}

	public Date getVisittime() {
		return visittime;
	}

	public void setVisittime(Date visittime) {
		this.visittime = visittime;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	private String rowguid;

	private String visiterip;

	private Date visittime;

	private String position;

}
