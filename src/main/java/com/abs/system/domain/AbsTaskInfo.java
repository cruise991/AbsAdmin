package com.abs.system.domain;

import java.util.Date;

import jakarta.persistence.Table;



@Table(name = "abs_taskinfo")
public class AbsTaskInfo {
	
	
	private String rowguid;
	private String taskname;
	private String taskclass;
	private Date addtime;
	private int runstatus;
	private String cronstr;
	private String remark;
	private int restartflag;
	private Date restarttime;
	
	
	
	public String getRowguid() {
		return rowguid;
	}
	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getTaskclass() {
		return taskclass;
	}
	public void setTaskclass(String taskclass) {
		this.taskclass = taskclass;
	}

	public int getRunstatus() {
		return runstatus;
	}
	public void setRunstatus(int runstatus) {
		this.runstatus = runstatus;
	}
	public String getCronstr() {
		return cronstr;
	}
	public void setCronstr(String cronstr) {
		this.cronstr = cronstr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getRestartflag() {
		return restartflag;
	}
	public void setRestartflag(int restartflag) {
		this.restartflag = restartflag;
	}
	public Date getRestarttime() {
		return restarttime;
	}
	public void setRestarttime(Date restarttime) {
		this.restarttime = restarttime;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	
	
}
