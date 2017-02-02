package com.fundin.dao.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import com.fundin.domain.entity.FundinAction;

public interface FundinActionDao {
	
	@Insert("insert into fundin_action (user_id, type, content, time) values "
			+ "(#{userId}, #{type}, #{content}, #{time})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int saveAction(FundinAction action);
	
}
