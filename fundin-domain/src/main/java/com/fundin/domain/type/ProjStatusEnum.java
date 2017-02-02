package com.fundin.domain.type;

public enum ProjStatusEnum {

	STARTING(0, "发起中"),
	AUDITING(1, "审核中"),
	FUNDING(2, "众筹中"),
	FUND_SUCC(3, "众筹成功"),
	FUND_FAIL(4, "众筹失败");
	
	private int code;
	private String desc;
	
	private ProjStatusEnum(int code, String desc) {
		this.setCode(code);
		this.setDesc(desc);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
