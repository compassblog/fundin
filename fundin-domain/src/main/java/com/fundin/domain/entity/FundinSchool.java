package com.fundin.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FundinSchool extends IdEntity {
	
	private String province;
	private String univ;
	private String school;
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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
