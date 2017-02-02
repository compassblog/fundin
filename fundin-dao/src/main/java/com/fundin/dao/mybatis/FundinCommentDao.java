package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fundin.domain.entity.FundinComment;

public interface FundinCommentDao {
	
	@Insert("insert into fundin_comment (user_id, proj_id, reply_to_id, reply_user_id, msg, create_time) values "
			+ "(#{userId}, #{projId}, #{replyToId}, #{replyUserId}, #{msg}, #{createTime})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int newComment(FundinComment comment);
	
	@Select("select id as id, user_id as userId, proj_id as projId, reply_to_id as replyToId, "
			+ "reply_user_id as replyUserId, msg as msg, create_time as createTime "
			+ "from fundin_comment where proj_id = #{projId} order by id desc")
	public List<FundinComment> getCommentList(@Param("projId") Long projId);
	
	@Delete("delete from fundin_comment where id = #{commentId}")
	public int delComment(@Param("commentId") Long commentId);
	
}
