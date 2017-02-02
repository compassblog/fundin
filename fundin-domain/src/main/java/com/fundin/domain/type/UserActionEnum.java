package com.fundin.domain.type;

public enum UserActionEnum {

	USER_ACTION_FOLLOW(1, "关注"),
	USER_ACTION_GOOD(2, "点赞");
	
	private int code;
	private String desc;
	
	private UserActionEnum(int code, String desc) {
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
