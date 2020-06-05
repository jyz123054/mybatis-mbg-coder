package com.test.mbg.entity;

import com.test.mbg.discriminator.entity.HealthReport;

public class THealthReportMale extends HealthReport {
    private String checkProject;

    private String detail;

    private Integer userId;

    public String getCheckProject() {
        return checkProject;
    }

    public void setCheckProject(String checkProject) {
        this.checkProject = checkProject;
    }
    
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

	@Override
	public String toString() {
		return "THealthReportMale [checkProject=" + checkProject + ", detail=" + detail + ", userId=" + userId + "]";
	}
    
}