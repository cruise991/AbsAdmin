package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;


@Table(name = "abs_fileinfo")
public class AbsFileInfo {

	private String rowguid;

	private String filepath;

	private String fileurl;

	private Date addtime;

	private long filesize;

	private String cliengguid;

	private String istoali;

	private String filename;

	private String filetype;

	private String firstkey;

	private int istmp;

	private String remark;

	private String userguid;

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getIstmp() {
		return istmp;
	}

	public void setIstmp(int istmp) {
		this.istmp = istmp;
	}

	public String getFirstkey() {
		return firstkey;
	}

	public void setFirstkey(String firstkey) {
		this.firstkey = firstkey;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getCliengguid() {
		return cliengguid;
	}

	public void setCliengguid(String cliengguid) {
		this.cliengguid = cliengguid;
	}

	public String getIstoali() {
		return istoali;
	}

	public void setIstoali(String istoali) {
		this.istoali = istoali;
	}

}
