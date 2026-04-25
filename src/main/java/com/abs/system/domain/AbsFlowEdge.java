package com.abs.system.domain;

import jakarta.persistence.Table;


@Table(name = "abs_flow_edge")
public class AbsFlowEdge {
	private String rowguid;
	private String source;
	private String sourcehandle;
	private String target;
	private String id;
	private String flowguid;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourcehandle() {
		return sourcehandle;
	}

	public void setSourcehandle(String sourcehandle) {
		this.sourcehandle = sourcehandle;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlowguid() {
		return flowguid;
	}

	public void setFlowguid(String flowguid) {
		this.flowguid = flowguid;
	}

}
