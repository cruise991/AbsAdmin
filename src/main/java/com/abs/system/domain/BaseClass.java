package com.abs.system.domain;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("base_class")
public class BaseClass {

    private String rowguid;
    private String className;
    private String grade;
    private Integer gradeYear;
    private Integer status;
    @TableField(value = "remark", exist = true)
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getGradeYear() {
        return gradeYear;
    }

    public void setGradeYear(Integer gradeYear) {
        this.gradeYear = gradeYear;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

	@Override
	public String toString() {
		return "BaseClass [rowguid=" + rowguid + ", className=" + className + ", grade=" + grade + ", gradeYear="
				+ gradeYear + ", status=" + status + ", remark=" + remark + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}


    
    
}