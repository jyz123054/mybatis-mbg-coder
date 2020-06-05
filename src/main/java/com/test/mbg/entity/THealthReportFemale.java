package com.test.mbg.entity;

import java.math.BigDecimal;

import com.test.mbg.discriminator.entity.HealthReport;

public class THealthReportFemale extends HealthReport {
    private String item;

    private BigDecimal score;

    private Integer userId;
    
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

	@Override
	public String toString() {
		return "THealthReportFemale [item=" + item + ", score=" + score + ", userId=" + userId + "]";
	}
}