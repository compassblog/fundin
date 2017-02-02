package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.fundin.dao.mybatis.provider.FundinMessageProvider;
import com.fundin.domain.entity.FundinMessage;

public interface FundinMessageDao {

	@Select("select count(*) from fundin_message where user_id = #{userId} and status = 0")
	public int getMessageCount(@Param("userId") Long userId);
	
	@Select("select id as id, proj_id as projId, user_id as userId, type as type, "
			+ "proj_title as projTitle, status as status, time as time from fundin_message "
			+ "where user_id = #{userId} and status = 0")
	public List<FundinMessage> getMessageList(@Param("userId") Long userId);
	
	@Insert("insert into fundin_message (proj_id, user_id, proj_title, type, time) "
			+ "VALUES (#{projId}, #{userId}, #{projTitle}, #{type}, now())")
	public int insert(FundinMessage message);
	
	@InsertProvider(type = FundinMessageProvider.class, method = "batchInsert")
	public int batchInsert(@Param("messages") List<FundinMessage> messages);
	
	@UpdateProvider(type = FundinMessageProvider.class, method = "updateStatus")
	public void updateStatus(@Param("userId") Long userId, @Param("projId") Long projId, 
			@Param("type") String type);
	
}
