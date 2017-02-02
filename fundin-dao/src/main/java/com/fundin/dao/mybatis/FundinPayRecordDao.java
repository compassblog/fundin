package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fundin.domain.entity.FundinPayRecord;
import com.fundin.domain.entity.FundinProj;
import com.fundin.domain.entity.FundinUser;
import com.fundin.domain.export.ExportEntity.ExportPay;

import com.fundin.domain.query.Pagination;

public interface FundinPayRecordDao {

	@Insert("insert into fundin_pay_record (proj_id, user_id, order_id, "
			+ "amount, way, time, primary_amount, redpacket_amount) values "
			+ "(#{projId}, #{userId}, #{orderId}, "
			+ "#{amount}, #{way}, #{time}, #{primaryAmount}, #{redpacketAmount})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int addPayRecord(FundinPayRecord payRecord);

	@Select("select id as id, proj_id as projId, user_id as userId, "
			+ "order_id as orderId, amount as amount, way as way, time as time from "
			+ "fundin_pay_record where user_id = #{userId} and proj_id = #{projId}")
	public FundinPayRecord queryOne(@Param("userId") Long userId, 
			@Param("projId") Long projId);
	
	@Select("select count(*) from fundin_pay_record where user_id = #{userId}")
	public int queryProIdCount(@Param("userId") Long userId);
	
	@Select("select proj_id as projId from fundin_pay_record where user_id = ${queryParams.userId} "
			+ "limit #{startIndex}, #{pageSize}")
	public List<Long> queryProIdList(Pagination<FundinProj> pageQuery);
	
	@Select("select user_id as userId from fundin_pay_record where proj_id = #{projId}")
	public List<Long> queryUserIdList(@Param("projId") Long projId);
	
	@Select("select user_id as userId from fundin_pay_record where proj_id = ${queryParams.projId} "
			+ "limit #{startIndex}, #{pageSize}")
	public List<Long> queryUserIdListByProjId(Pagination<FundinUser> pageQuery);
	
	@Select("select count(0) from fundin_pay_record where proj_id = #{projId}")
	public int queryRepayUserCount(@Param("projId") Long projId);
	
	@Select("select B.title as projName, C.name as userName, A.amount as amount "
			+ "from fundin_pay_record A "
			+ "join fundin_proj B on A.proj_id = B.id "
			+ "join fundin_user C on A.user_id = C.id ")
	public List<ExportPay> exportPay();
	
	@Select("select ifnull(sum(redpacket_amount),0) from fundin_pay_record where proj_id=#{projId}")
	public int getRedPacketAmount(@Param("projId") Long projId);
	
	@Select("select amount+redpacket_amount from fundin_pay_record where user_id=#{userId} and proj_id=#{projId}")
	public int getSupportAmount(@Param("userId") Long userId, @Param("projId") Long projId);
}
