package com.fundin.domain.dto;

public class ProjInfo {

	private String restTime;
	private boolean hasGood = false;
	private boolean hasFollow = false;
	private boolean hasSupport = false;
	private Integer goodNum;
	private Integer followNum;
	private String province;
	
	public String getRestTime() {
		return restTime;
	}
	public void setRestTime(String restTime) {
		this.restTime = restTime;
	}
	public boolean isHasGood() {
		return hasGood;
	}
	public void setHasGood(boolean hasGood) {
		this.hasGood = hasGood;
	}
	public boolean isHasFollow() {
		return hasFollow;
	}
	public void setHasFollow(boolean hasFollow) {
		this.hasFollow = hasFollow;
	}
	public Integer getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(Integer goodNum) {
		this.goodNum = goodNum;
	}
	public Integer getFollowNum() {
		return followNum;
	}
	public void setFollowNum(Integer followNum) {
		this.followNum = followNum;
	}
	public boolean isHasSupport() {
		return hasSupport;
	}
	public void setHasSupport(boolean hasSupport) {
		this.hasSupport = hasSupport;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
}
