package com.fundin.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ProjSortItem {
	
	private Long projId;
	private String title;
	private BigDecimal raisedRatio;
	private Integer days;
	private Date startDate;
	
	private Integer weight;
	
	public Long getProjId() {
		return projId;
	}
	public void setProjId(Long projId) {
		this.projId = projId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BigDecimal getRaisedRatio() {
		return raisedRatio;
	}
	public void setRaisedRatio(BigDecimal raisedRatio) {
		this.raisedRatio = raisedRatio;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
}
