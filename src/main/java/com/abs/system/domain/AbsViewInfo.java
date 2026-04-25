package com.abs.system.domain;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Table;

@Table(name = "abs_viewinfo")
public class AbsViewInfo {

	private String rowguid;

	private String viewname;

	private String url;

	private String images;

	private String sortnum;

	private String parentguid;

	private String parentname;

	private LocalDateTime addtime;

	private String remark;

	public LocalDateTime getAddtime() {
		return addtime;
	}

	public void setAddtime(LocalDateTime addtime) {
		this.addtime = addtime;
	}

	private String isroot;

	public String getIsroot() {
		return isroot;
	}

	public void setIsroot(String isroot) {
		this.isroot = isroot;
	}

	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	public String getViewname() {
		return viewname;
	}

	public void setViewname(String viewname) {
		this.viewname = viewname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getSortnum() {
		return sortnum;
	}

	public void setSortnum(String sortnum) {
		this.sortnum = sortnum;
	}

	public String getParentguid() {
		return parentguid;
	}

	public void setParentguid(String parentguid) {
		this.parentguid = parentguid;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
