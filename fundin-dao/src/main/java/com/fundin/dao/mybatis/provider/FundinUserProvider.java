package com.fundin.dao.mybatis.provider;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SqlBuilder;

import com.fundin.domain.entity.FundinUser;

@SuppressWarnings("deprecation")
public class FundinUserProvider extends SqlBuilder {
	
	public String updateSomeInfo(Map<String, Object> params) {
		FundinUser user = (FundinUser) params.get("user");
		StringBuilder sb = new StringBuilder();
		sb.append("update fundin_user set ");
		
		if (StringUtils.isNotBlank(user.getHeadImg())) {
			sb.append("head_img = '").append(user.getHeadImg()).append("', ");
		}
		if (null != user.getSex()) {
			sb.append("sex = ").append(user.getSex()).append(", ");
		}
		if (StringUtils.isNotBlank(user.getBirthday())) {
			sb.append("birthday = '").append(user.getBirthday()).append("', ");
		}
		if (StringUtils.isNotBlank(user.getSign())) {
			sb.append("sign = '").append(user.getSign()).append("', ");
		}
		if (StringUtils.isNotBlank(user.getPhone())) {
			sb.append("phone = '").append(user.getPhone()).append("', ");
		}
		if (StringUtils.isNotBlank(user.getBankAccount())) {
			sb.append("bank_account = '").append(user.getBankAccount()).append("', ");
		}
		if (null != user.getSchoolId()) {
			sb.append("school_id = '").append(user.getSchoolId()).append("', ");
		}
		if (null != user.getEntryYear()) {
			sb.append("entry_year = '").append(user.getEntryYear()).append("', ");
		}
		if (null != user.getBankName()) {
			sb.append("bank_name = '").append(user.getBankName()).append("', ");
		}
		if (null != user.getBankUserName()) {
			sb.append("bank_user_name = '").append(user.getBankUserName()).append("', ");
		}
		sb.append("update_time = '").append(DateTool.dateFormat(new Date())).append("' ");
		
		sb.append("where id = ").append(user.getId());
		return sb.toString();
	}
	
}
