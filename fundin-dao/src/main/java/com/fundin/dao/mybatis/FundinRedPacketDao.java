package com.fundin.dao.mybatis;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.fundin.domain.dto.RedPacketInfo;
import com.fundin.domain.entity.FundinRedPacket;

public interface FundinRedPacketDao {

	@Insert("INSERT INTO fundin_redpacket (user_id, `type`,amount, `status`,create_time,begin_time,end_time)"
			+ " VALUES(#{userId},#{type},#{amount},#{status},#{createTime},#{beginTime},#{endTime})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int createRedPacket(FundinRedPacket redPacket);

	@Select("select ifnull(sum(amount),0) from fundin_redpacket where user_id=#{userId} and type=#{type}")
	public Long checkAmount(@Param("userId") Long userId, @Param("type") Integer type);

	@Select("select id,type,amount,count(0) count,end_time endTime,begin_time beginTime from "
			+ "fundin_redpacket where user_id =#{userId} and status=1 group by type,amount")
	public List<RedPacketInfo> getMyRedPacketInfo(@Param("userId") Long userId);
	
	@Select("select type from fundin_redpacket where user_id=#{userId} and proj_id=#{projId} and status=2 group by type,amount")
	public List<Integer> getUsedRedPacket(@Param("userId") Long userId, @Param("projId") Long projId);
	
	@Update("update fundin_redpacket set proj_id=#{projId},status=#{status},used_time=#{now} where id=#{id}")
	public void updateInfoById(@Param("id") Long id, @Param("projId") Long projId, @Param("status") Integer status,
			@Param("now") Date now);
	
	@Update("update fundin_redpacket set proj_id=#{projId},status=#{status},used_time=#{now} "
			+ "where id=(select id from fundin_redpacket where user_id=#{userId} and amount=#{amount} limit 0,1)")
	public void updateInfoByAmount(@Param("userId") Long userId, @Param("projId") Long projId, @Param("amount") Integer amount,
			@Param("status") Integer status, @Param("now") Date now);
}
