package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fundin.domain.entity.FundinProjProgress;

public interface FundinProjProgressDao {

	@Select("select id as id, proj_id as projId, user_id as userId, progress_desc as progressDesc, "
			+ "time as time from fundin_proj_progress where proj_id = #{projId}")
	public List<FundinProjProgress> getProgressList(@Param("projId") Long projId);
	
	@Insert("insert into fundin_proj_progress (proj_id, user_id, progress_desc, time) "
			+ "values (#{projId}, #{userId}, #{progressDesc}, #{time})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int addNewProgress(FundinProjProgress progress);
	
}
