package com.abs.system.domain;

import jakarta.persistence.Table;



@Table(name = "abs_viewrole")
public class AbsViewRole {

	private String rowguid;

	private String roleguid;

	private String viewguid;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getRoleguid() {
		return roleguid;
	}

	public void setRoleguid(String roleguid) {
		this.roleguid = roleguid;
	}

	public String getViewguid() {
		return viewguid;
	}

	public void setViewguid(String viewguid) {
		this.viewguid = viewguid;
	}

}
