package com.fundin.domain.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundinAttention extends IdEntity {
	
	private Long userId;
	private Long attentionUserId;
	private Date time;
	
	private String userName;
	private String userHeadImg;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Long getAttentionUserId() {
		return attentionUserId;
	}

	public void setAttentionUserId(Long attentionUserId) {
		this.attentionUserId = attentionUserId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHeadImg() {
		return userHeadImg;
	}

	public void setUserHeadImg(String userHeadImg) {
		this.userHeadImg = userHeadImg;
	}

}
