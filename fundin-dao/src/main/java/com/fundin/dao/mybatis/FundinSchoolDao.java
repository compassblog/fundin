package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.fundin.domain.entity.FundinSchool;

public interface FundinSchoolDao {

	@Select("select id as id, province as province, univ as univ, school as school "
			+ "from fundin_school")
	public List<FundinSchool> queryAll();
	
	@Insert("insert into fundin_school (province, univ, school) "
			+ "values (#{province}, #{univ}, #{school})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int insert(FundinSchool entity);
	
}
