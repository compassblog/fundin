package com.fundin.domain.type;

public enum ProjLocationEnum {

	LOCATION_ALL("national", "全国"),
	LOCATION_1("北京", "北京"),
	LOCATION_2("上海", "上海"),
	LOCATION_3("四川", "四川"),
	LOCATION_4("浙江", "浙江"),
	LOCATION_5("江苏", "江苏"),
	LOCATION_6("重庆", "重庆"),
	LOCATION_7("江西", "江西");
	
	private String clause;
	private String desc;
	
	private ProjLocationEnum(String clause, String desc) {
		this.setClause(clause);
		this.setDesc(desc);
	}

	public static ProjLocationEnum[] locationValues() {
		return ProjLocationEnum.values();
	}
	
	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
