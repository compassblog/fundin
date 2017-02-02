package com.fundin.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundinProj extends IdEntity {
	
	private Long userId;
	private Integer subject;
	private String title;
	private String intro;
	private String coverImg;
	private String video;
	private String content;
	private Integer totalAmount;
	private Integer raisedAmount;
	private BigDecimal raisedRatio;
	private Integer raisedNum;
	private Integer days;
	private Integer repayDays;
	private Integer repayWay;
	private Date startDate;
	private Date endDate;
	private Date updateDate;
	private Integer status;
	private String repayContent;
	private String repayImage;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getCoverImg() {
		return coverImg;
	}
	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getRaisedAmount() {
		return raisedAmount;
	}
	public void setRaisedAmount(Integer raisedAmount) {
		this.raisedAmount = raisedAmount;
	}
	public Integer getRaisedNum() {
		return raisedNum;
	}
	public void setRaisedNum(Integer raisedNum) {
		this.raisedNum = raisedNum;
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
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getSubject() {
		return subject;
	}
	public void setSubject(Integer subject) {
		this.subject = subject;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getRepayDays() {
		return repayDays;
	}
	public void setRepayDays(Integer repayDays) {
		this.repayDays = repayDays;
	}
	public Integer getRepayWay() {
		return repayWay;
	}
	public void setRepayWay(Integer repayWay) {
		this.repayWay = repayWay;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public String getRepayContent() {
		return repayContent;
	}
	public void setRepayContent(String repayContent) {
		this.repayContent = repayContent;
	}
	public String getRepayImage() {
		return repayImage;
	}
	public void setRepayImage(String repayImage) {
		this.repayImage = repayImage;
	}
	
}
