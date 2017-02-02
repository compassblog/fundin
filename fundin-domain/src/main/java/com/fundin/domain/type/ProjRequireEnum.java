package com.fundin.domain.type;

public enum ProjRequireEnum {

	REQUIRE_NEW("new", "最新上线"),
	REQUIRE_HOT("hot", "热门"),
	REQUIRE_ENDSOON("endsoon", "即将结束"),
	REQUIRE_FINISHED("finished", "已经完成");
	
	private String clause;
	private String desc;
	
	private ProjRequireEnum(String clause, String desc) {
		this.setClause(clause);
		this.setDesc(desc);
	}

	public static ProjRequireEnum[] requireValues() {
		return ProjRequireEnum.values();
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
