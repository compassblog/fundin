package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fundin.domain.entity.FundinProj;
import com.fundin.domain.entity.FundinUserAction;
import com.fundin.domain.export.ExportEntity.ExportAction;
import com.fundin.domain.query.Pagination;

public interface FundinUserActionDao {
	
	@Insert("insert into fundin_user_action (user_id, proj_id, type, time) values "
			+ "(#{userId}, #{projId}, #{type}, #{time})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int saveUserAction(FundinUserAction userAction);
	
	@Select("select user_id as userId, proj_id as projId, type as type, "
			+ "time as time from fundin_user_action "
			+ "where proj_id = #{projId} and type = #{type}")
	public List<FundinUserAction> getUserActionList(@Param("projId") Long projId, 
			@Param("type") Integer type);
	
	@Select("select count(*) from fundin_user_action "
			+ "where user_id = ${userId} and type = ${type}")
	public int getProIdCount(@Param("userId") Long userId, 
			@Param("type") Integer type);
	
	@Select("select proj_id as projId from fundin_user_action "
			+ "where user_id = ${queryParams.userId} "
			+ "and type = ${queryParams.type} limit #{startIndex}, #{pageSize}")
	public List<Long> getProIdList(Pagination<FundinProj> pageQuery);
	
	@Select("select B.title as projName, C.name as userName "
			+ "from fundin_action A "
			+ "join fundin_proj B on A.proj_id = B.id "
			+ "join fundin_user C on A.user_id = C.id "
			+ "where A.type = #{type}")
	public List<ExportAction> exportUserActionByType(@Param("type") Integer type);
	
}
