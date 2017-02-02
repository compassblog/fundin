package com.fundin.domain.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundinComment extends IdEntity {
	
	private Long projId;
	private Long userId;
	private Long replyToId;
	private Long replyUserId;
	private String msg;
	private Date createTime;
	
	public Long getReplyToId() {
		return replyToId;
	}
	public void setReplyToId(Long replyToId) {
		this.replyToId = replyToId;
	}
	public Long getReplyUserId() {
		return replyUserId;
	}
	public void setReplyUserId(Long replyUserId) {
		this.replyUserId = replyUserId;
	}
	public Long getProjId() {
		return projId;
	}
	public void setProjId(Long projId) {
		this.projId = projId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
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
	
}
