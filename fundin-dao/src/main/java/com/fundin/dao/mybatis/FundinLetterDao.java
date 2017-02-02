package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fundin.dao.mybatis.provider.FundinLetterProvider;
import com.fundin.domain.entity.FundinLetter;
import com.fundin.domain.query.Pagination;

public interface FundinLetterDao {

	@Select("select count(*) from fundin_letter where user_id = #{userId}")
	public int getLetterCount(@Param("userId") Long userId);
	
	@Select("select id as id, proj_id as projId, user_id as userId, title as title, "
			+ "content as content, time as time from fundin_letter "
			+ "where user_id = #{queryParams.userId} order by id desc "
			+ "limit #{startIndex}, #{pageSize}")
	public List<FundinLetter> getLetterList(Pagination<FundinLetter> pageQuery);
	
	@InsertProvider(type = FundinLetterProvider.class, method = "batchInsert")
	public int batchInsert(@Param("letters") List<FundinLetter> letters);
	
	@Insert("insert into fundin_letter (proj_id, user_id, title, content, time) "
			+ "VALUES (#{projId}, #{userId}, #{title}, #{content}, now())")
	public int insert(FundinLetter letter);
	
}
