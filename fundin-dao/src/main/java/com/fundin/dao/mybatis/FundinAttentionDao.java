package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fundin.domain.entity.FundinAttention;
import com.fundin.domain.query.Pagination;

public interface FundinAttentionDao {

	@Select("select count(*) from fundin_attention where user_id = #{userId}")
	public int getByUserIdCount(@Param("userId") Long userId);
	
	@Select("select id as id, user_id as userId, attention_user_id as attentionUserId, "
			+ "time as time from fundin_attention where user_id = ${queryParams.userId} "
			+ "limit #{startIndex}, #{pageSize}")
	public List<FundinAttention> getByUserId(Pagination<FundinAttention> pageQuery);
	
	@Select("select count(*) from fundin_attention where attention_user_id = #{attentionUserId}")
	public int getByAttentionUserIdCount(@Param("attentionUserId") Long attentionUserId);
	
	@Select("select id as id, user_id as userId, attention_user_id as attentionUserId, "
			+ "time as time from fundin_attention where attention_user_id = #{queryParams.attentionUserId} "
			+ "limit #{startIndex}, #{pageSize}")
	public List<FundinAttention> getByAttentionUserId(Pagination<FundinAttention> pageQuery);
	
	@Insert("insert into fundin_attention (user_id, attention_user_id, time) "
			+ "values (#{userId}, #{attentionUserId}, #{time})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int addNewAttention(FundinAttention attention);
	
	@Delete("delete from fundin_attention where user_id = #{userId} "
			+ "and attention_user_id = #{attentionUserId}")
	public void cancleAttention(FundinAttention attention);
	
	@Select("select id as id, user_id as userId, attention_user_id as attentionUserId, "
			+ "time as time from fundin_attention where user_id = #{userId} "
			+ "and attention_user_id = #{attentionUserId}")
	public FundinAttention hasAttention(@Param("userId") Long userId, 
			@Param("attentionUserId") Long attentionUserId);
	
}
