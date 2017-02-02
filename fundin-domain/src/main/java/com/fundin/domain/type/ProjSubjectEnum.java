package com.fundin.domain.type;

public enum ProjSubjectEnum {

	SUBJECT_PERSONAL(1, "个人创意"),
	SUBJECT_ORGANIZATION(2, "社团组织"),
	SUBJECT_PUBLIC(3, "校园公益"),
	SUBJECT_STARTUP(4, "创业项目"),
	
	ACTIVITY_POSTCARD(10, "明信片设计");
	
	private int code;
	private String desc;
	
	private ProjSubjectEnum(int code, String desc) {
		this.setCode(code);
		this.setDesc(desc);
	}

	public static ProjSubjectEnum[] subjectValues() {
		return ProjSubjectEnum.values();
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
