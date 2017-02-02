package com.fundin.domain.type;

public enum RedPacketTypeEnum {

	REDPACKET_TYPE_Z(1, "支持红包"),
	REDPACKET_TYPE_F(2, "发起红包");

	private int code;
	private String desc;

	private RedPacketTypeEnum(int code, String desc) {
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
