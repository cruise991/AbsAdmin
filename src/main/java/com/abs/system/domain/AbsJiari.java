package com.abs.system.domain;

import jakarta.persistence.Table;


@Table(name = "abs_jiari")
public class AbsJiari {
	private String rowguid;
	private int isjia;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public int getIsjia() {
		return isjia;
	}

	public void setIsjia(int isjia) {
		this.isjia = isjia;
	}

}
