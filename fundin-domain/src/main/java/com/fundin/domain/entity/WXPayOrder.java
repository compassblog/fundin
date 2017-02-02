package com.fundin.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class WXPayOrder extends IdEntity {

	private Long userId;
	private Long projId;
	private String out_trade_no;
	private String total_fee;
	private String time_start;
	private String time_expire;
	private String product_id;
	private String code_url;
	private String prepay_id;
	private String openid;
	private String transaction_id;
	
	private String out_refund_no;
	private String refund_id;
	
	/**
	 * 订单状态（0：生成未支付；1：确认已支付；2：已退款）
	 */
	private Integer status;
	
	private Integer primaryAmount;//原始支付金额
	private Integer redpacketAmount;//红包金额

	public Long getProjId() {
		return projId;
	}

	public void setProjId(Long projId) {
		this.projId = projId;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getCode_url() {
		return code_url;
	}

	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getPrimaryAmount() {
		return primaryAmount;
	}

	public void setPrimaryAmount(Integer primaryAmount) {
		this.primaryAmount = primaryAmount;
	}

	public Integer getRedpacketAmount() {
		return redpacketAmount;
	}

	public void setRedpacketAmount(Integer redpacketAmount) {
		this.redpacketAmount = redpacketAmount;
	}
	
}
