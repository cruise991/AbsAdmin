package com.abs.article.domain;

import java.util.Date;

import jakarta.persistence.Table;

@Table(name = "abs_article")
public class AbsArticle {

	private int id;

	private String rowguid;

	private String title;

	private String content;

	private String author;

	private String btype;

	private String summary;

	private String status;

	private String fmguid;

	private String fmurl;

	private Date addtime;

	public String getFmurl() {
		return fmurl;
	}

	public void setFmurl(String fmurl) {
		this.fmurl = fmurl;
	}

	public String getBtype() {
		return btype;
	}

	public void setBtype(String btype) {
		this.btype = btype;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public String getFmguid() {
		return fmguid;
	}

	public void setFmguid(String fmguid) {
		this.fmguid = fmguid;
	}

}
