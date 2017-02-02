package com.fundin.pay.wx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WXRefundReq {
	
	/**
	 * 微信分配的公众账号ID
	 */
	@XmlElement(name = "appid")
	private String appid;
	
	/**
	 * 微信支付分配的商户号
	 */
	@XmlElement(name = "mch_id")
	private String mch_id;

	/**
	 * 终端设备号(PC网页或公众号内支付请传"WEB")
	 */
	@XmlElement(name = "device_info")
	private String device_info;
	
	/**
	 * 随机字符串，不长于32位
	 */
	@XmlElement(name = "nonce_str")
	private String nonce_str;
	
	/**
	 * 签名
	 */
	@XmlElement(name = "sign")
	private String sign;
	
	/**
	 * 微信支付订单号
	 */
	@XmlElement(name = "transaction_id")
	private String transaction_id;
	
	/**
	 * 商户系统内部的订单号
	 */
	@XmlElement(name = "out_trade_no")
	private String out_trade_no;
	
	/**
	 * 商户系统内部的退款单号
	 */
	@XmlElement(name = "out_refund_no")
	private String out_refund_no;
	
	/**
	 * 订单总金额，单位为分
	 */
	@XmlElement(name = "total_fee")
	private String total_fee;
	
	/**
	 * 退款总金额，单位为分
	 */
	@XmlElement(name = "refund_fee")
	private String refund_fee;
	
	/**
	 * 默认人民币：CNY
	 */
	@XmlElement(name = "refund_fee_type")
	private String refund_fee_type;
	
	/**
	 * 操作员帐号, 默认为商户号
	 */
	@XmlElement(name = "op_user_id")
	private String op_user_id;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getRefund_fee_type() {
		return refund_fee_type;
	}

	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
	}

	public String getOp_user_id() {
		return op_user_id;
	}

	public void setOp_user_id(String op_user_id) {
		this.op_user_id = op_user_id;
	}
	
}
