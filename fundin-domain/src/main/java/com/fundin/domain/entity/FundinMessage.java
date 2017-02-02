package com.fundin.domain.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundinMessage extends IdEntity {
	
	private Long projId;
	private Long userId;
	private String projTitle;
	private String type;
	private Integer status;
	private Date time;
	
	public Long getProjId() {
		return projId;
	}

	public void setProjId(Long projId) {
		this.projId = projId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getProjTitle() {
		return projTitle;
	}

	public void setProjTitle(String projTitle) {
		this.projTitle = projTitle;
	}

}
