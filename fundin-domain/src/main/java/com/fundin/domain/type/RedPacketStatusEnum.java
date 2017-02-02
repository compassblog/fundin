package com.fundin.domain.type;

public enum RedPacketStatusEnum {

	REDPACKET_UNUSED(1, "可用"),
	REDPACKET_USED(2, "已用"),
	REDPACKER_OVERDUE(3, "过期");

	private int code;
	private String desc;

	private RedPacketStatusEnum(int code, String desc) {
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
