package com.fundin.domain.type;

public enum ActionEnum {

	ACTION_FEEDBACK(1, "用户反馈"),
	ACTION_WITHDRAW(2, "申请提现");
	
	private int code;
	private String desc;
	
	private ActionEnum(int code, String desc) {
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
