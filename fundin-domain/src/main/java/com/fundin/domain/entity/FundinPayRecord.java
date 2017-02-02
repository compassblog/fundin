package com.fundin.domain.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundinPayRecord extends IdEntity {
	
	private Long projId;
	private Long userId;
	private Long orderId;
	private Integer amount;
	private Integer way;
	private Date time;
	
	private Integer primaryAmount;//原始支付金额
	private Integer redpacketAmount;//红包金额
	
	public Long getProjId() {
		return projId;
	}
	public void setProjId(Long projId) {
		this.projId = projId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getWay() {
		return way;
	}
	public void setWay(Integer way) {
		this.way = way;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
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
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getPrimaryAmount() {
		return primaryAmount;
	}
	public void setPrimaryAmount(Integer primaryAmount) {
		this.primaryAmount = primaryAmount;
	}
	public Integer getRedpacketAmount() {
		return redpacketAmount;
	}
	public void setRedpacketAmount(Integer redpacketAmount) {
		this.redpacketAmount = redpacketAmount;
	}
	
}
