package com.fundin.dao.mybatis.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SqlBuilder;

import com.fundin.domain.entity.FundinLetter;

@SuppressWarnings("deprecation")
public class FundinLetterProvider extends SqlBuilder {
	
	@SuppressWarnings("unchecked")
	public String batchInsert(Map<String, Object> params) {
		List<FundinLetter> letters = (List<FundinLetter>) params.get("letters");
		
		StringBuilder sb = new StringBuilder();
		sb.append("insert into fundin_letter (proj_id, user_id, title, content, time) VALUES");
		for (FundinLetter letter : letters) {
			sb.append("(").append(letter.getProjId()).append(",\"").
				append(letter.getUserId()).append("\",\"").append(letter.getTitle()).
				append("\",\"").append(letter.getContent()).append("\",\"").
				append(DateTool.dateFormat(new Date())).append("\"),");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}
	
}
