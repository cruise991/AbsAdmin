package com.abs.system.domain;

import jakarta.persistence.Table;



@Table(name = "abs_flow_node")
public class AbsFlowNode {
	private String rowguid;
	private String id;
	private String type;
	private String position;
	private String data;
	private String measured;
	private String selected;
	private String dragging;
	private String flowguid;

	public String getRowguid() {
		return rowguid;
	}

	public void setRowguid(String rowguid) {
		this.rowguid = rowguid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMeasured() {
		return measured;
	}

	public void setMeasured(String measured) {
		this.measured = measured;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getDragging() {
		return dragging;
	}

	public void setDragging(String dragging) {
		this.dragging = dragging;
	}

	public String getFlowguid() {
		return flowguid;
	}

	public void setFlowguid(String flowguid) {
		this.flowguid = flowguid;
	}

}
