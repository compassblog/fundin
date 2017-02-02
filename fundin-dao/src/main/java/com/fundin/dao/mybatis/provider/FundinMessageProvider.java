package com.fundin.dao.mybatis.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SqlBuilder;

import com.fundin.domain.entity.FundinMessage;

@SuppressWarnings("deprecation")
public class FundinMessageProvider extends SqlBuilder {
	
	@SuppressWarnings("unchecked")
	public String batchInsert(Map<String, Object> params) {
		List<FundinMessage> messages = (List<FundinMessage>) params.get("messages");
		
		StringBuilder sb = new StringBuilder();
		sb.append("insert into fundin_message (proj_id, user_id, proj_title, type, time) VALUES");
		for (FundinMessage message : messages) {
			sb.append("(").append(message.getProjId()).append(",").
				append(message.getUserId()).append(",\"").append(message.getProjTitle()).
				append("\",\"").append(message.getType()).append("\",\"").
				append(DateTool.dateFormat(new Date())).append("\"),");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}
	
	public String updateStatus(Map<String, Object> params) {
		BEGIN();
		UPDATE(" fundin_message ");
		SET(" status = 1 ");
		WHERE(" user_id = #{userId} ");
		if (null != params.get("type")) {
			WHERE(" type = #{type} ");
		}
		if (null != params.get("projId")) {
			WHERE(" proj_id = #{projId} ");
		}
		return SQL();
	}
	
}
