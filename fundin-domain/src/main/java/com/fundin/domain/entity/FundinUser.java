package com.fundin.domain.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundinUser extends IdEntity {
	
	private String name;
	private String passwd;
	private String salt;
	private Integer type;
	private String email;
	private String phone;
	private String headImg;
	private Integer sex;
	private String birthday;
	private String sign;
	private Long schoolId;
	private String entryYear;
	private Date regTime;
	private Date updateTime;
	private String bankName;
	private String bankAccount;
	private String bankUserName;
	private Integer raisedAmount;
	private Integer withdrawalAmount;
	private Integer depositAmount;
	private Integer transactionAmount;
	private Long inviteUserId;
	private Integer inviteCount;

	private String constellation;
	private FundinSchool schoolModel;
	
	private Integer supportAmount;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEntryYear() {
		return entryYear;
	}
	public void setEntryYear(String entryYear) {
		this.entryYear = entryYear;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankUserName() {
		return bankUserName;
	}
	public void setBankUserName(String bankUserName) {
		this.bankUserName = bankUserName;
	}
	public Integer getRaisedAmount() {
		return raisedAmount;
	}
	public void setRaisedAmount(Integer raisedAmount) {
		this.raisedAmount = raisedAmount;
	}
	public Integer getWithdrawalAmount() {
		return withdrawalAmount;
	}
	public void setWithdrawalAmount(Integer withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}
	public Integer getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(Integer depositAmount) {
		this.depositAmount = depositAmount;
	}
	public Integer getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Integer transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public Long getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}
	
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	
	public FundinSchool getSchoolModel() {
		return schoolModel;
	}
	public void setSchoolModel(FundinSchool schoolModel) {
		this.schoolModel = schoolModel;
	}
	public Long getInviteUserId() {
		return inviteUserId;
	}
	public void setInviteUserId(Long inviteUserId) {
		this.inviteUserId = inviteUserId;
	}
	public Integer getInviteCount() {
		return inviteCount;
	}
	public void setInviteCount(Integer inviteCount) {
		this.inviteCount = inviteCount;
	}
	public Integer getSupportAmount() {
		return supportAmount;
	}
	public void setSupportAmount(Integer supportAmount) {
		this.supportAmount = supportAmount;
	}
	
}
