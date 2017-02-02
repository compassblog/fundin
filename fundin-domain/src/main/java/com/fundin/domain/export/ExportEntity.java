package com.fundin.domain.export;

import java.util.Date;

public class ExportEntity {

	public static class ExportAction {
		private String projName;
		private String userName;
		public String getProjName() {
			return projName;
		}
		public void setProjName(String projName) {
			this.projName = projName;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
	}
	
	public static class ExportPay extends ExportAction {
		private Integer amount;
		public Integer getAmount() {
			return amount;
		}
		public void setAmount(Integer amount) {
			this.amount = amount;
		}
	}
	
	public static class ExportUser {
		private String name;
		private Integer type;
		private String email;
		private String phone;
		private Integer sex;
		private String birthday;
		private String sign;
		private String entryYear;
		private Date regTime;
		private String province;
		private String univ;
		private String school;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
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
		public String getBirthday() {
			return birthday;
		}
		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
		public String getEntryYear() {
			return entryYear;
		}
		public void setEntryYear(String entryYear) {
			this.entryYear = entryYear;
		}
		public Date getRegTime() {
			return regTime;
		}
		public void setRegTime(Date regTime) {
			this.regTime = regTime;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getUniv() {
			return univ;
		}
		public void setUniv(String univ) {
			this.univ = univ;
		}
		public String getSchool() {
			return school;
		}
		public void setSchool(String school) {
			this.school = school;
		}
	}
	
	public static class ExportUserCount {
		private Date regTime;
		private Integer incr;
		private Integer total;
		public Integer getIncr() {
			return incr;
		}
		public void setIncr(Integer incr) {
			this.incr = incr;
		}
		public Integer getTotal() {
			return total;
		}
		public void setTotal(Integer total) {
			this.total = total;
		}
		public Date getRegTime() {
			return regTime;
		}
		public void setRegTime(Date regTime) {
			this.regTime = regTime;
		}
	}
	
}
