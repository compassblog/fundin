package com.fundin.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.fundin.domain.entity.WXPayOrder;

public interface WXPayOrderDao {

	@Insert("insert into wx_pay_order(user_id, proj_id, out_trade_no, total_fee, time_start, time_expire, "
			+ "product_id, code_url, prepay_id, openid, status, primary_amount, redpacket_amount) "
			+ "values(#{userId}, #{projId}, #{out_trade_no}, #{total_fee}, #{time_start}, #{time_expire}, "
			+ "#{product_id}, #{code_url}, #{prepay_id}, #{openid}, #{status}, #{primaryAmount}, #{redpacketAmount})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int createNewOrder(WXPayOrder payOrder);
	
	@Update("update wx_pay_order set transaction_id = #{transaction_id}, openid = #{openid}, status = #{status} "
			+ "where out_trade_no = #{out_trade_no}")
	public int updateFinishedOrder(@Param("transaction_id") String transaction_id, @Param("openid") String openid, 
			@Param("status") Integer status, @Param("out_trade_no") Long out_trade_no);
	
	@Update("update wx_pay_order set out_refund_no = #{out_refund_no}, refund_id = #{refund_id}, "
			+ "status = #{status} where out_trade_no = #{out_trade_no}")
	public int updateRefundOrder(@Param("out_refund_no") String out_refund_no, 
			@Param("refund_id") String refund_id, @Param("status") Integer status, 
			@Param("out_trade_no") Long out_trade_no);
	
	@Select("select id as id, user_id as userId, proj_id as projId, "
			+ "out_trade_no as out_trade_no, total_fee as total_fee, time_start as time_start, "
			+ "time_expire as time_expire, product_id as product_id, code_url as code_url, "
			+ "transaction_id as transaction_id, out_refund_no as out_refund_no, refund_id as refund_id, "
			+ "status as status, primary_amount as primaryAmount, redpacket_amount as redpacketAmount "
			+ "from wx_pay_order where out_trade_no = #{out_trade_no}")
	public WXPayOrder getPayOrder(@Param("out_trade_no") Long out_trade_no);
	
	@Select("select id as id, user_id as userId, proj_id as projId, "
			+ "out_trade_no as out_trade_no, total_fee as total_fee, time_start as time_start, "
			+ "time_expire as time_expire, product_id as product_id, code_url as code_url, "
			+ "transaction_id as transaction_id, out_refund_no as out_refund_no, refund_id as refund_id, "
			+ "status as status from wx_pay_order where id = #{wxPayId}")
	public WXPayOrder getPayOrderById(@Param("wxPayId") Long wxPayId);
	
	@Select("select id as id, user_id as userId, proj_id as projId, "
			+ "out_trade_no as out_trade_no, total_fee as total_fee, time_start as time_start, "
			+ "time_expire as time_expire, product_id as product_id, code_url as code_url, "
			+ "transaction_id as transaction_id, out_refund_no as out_refund_no, refund_id as refund_id, "
			+ "status as status from wx_pay_order where proj_id = #{projId} and status = 1")
	public List<WXPayOrder> getPayOrderByProjId(@Param("projId") Long projId);
	
}
