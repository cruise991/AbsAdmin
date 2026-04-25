package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;

@Table(name = "abs_msginfo")
public class AbsMsgInfo {

	private String rowguid;
	private String isread;
	private String msgtitle;
	private String content;
	private String userguid;
	private String roleguid;
	private Date addtime;
	private String datasource;
	private String eventguid;
	private String msgtype;

	public String getEventguid() {
		return eventguid;
	}

	public void setEventguid(String eventguid) {
		this.eventguid = eventguid;
	}

	public String getRoleguid() {
		return roleguid;
	}

	public void setRoleguid(String roleguid) {
		this.roleguid = roleguid;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}

	public String getMsgtitle() {
		return msgtitle;
	}

	public void setMsgtitle(String msgtitle) {
		this.msgtitle = msgtitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserguid() {
		return userguid;
	}

	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

}
