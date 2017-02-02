package com.fundin.pay.wx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WXRefundResp {
	
	/**
	 * SUCCESS/FAIL
		此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 */
	@XmlElement(name = "return_code")
	private String return_code;
	
	/**
	 * 返回信息，如非空，为错误原因
		签名失败
		参数格式校验错误
	 */
	@XmlElement(name = "return_msg")
	private String return_msg;
	
	/**
	 * 调用接口提交的公众账号ID
	 */
	@XmlElement(name = "appid")
	private String appid;
	
	/**
	 * 调用接口提交的商户号
	 */
	@XmlElement(name = "mch_id")
	private String mch_id;
	
	/**
	 * 调用接口提交的终端设备号
	 */
	@XmlElement(name = "device_info")
	private String device_info;
	
	/**
	 * 微信返回的随机字符串
	 */
	@XmlElement(name = "nonce_str")
	private String nonce_str;
	
	/**
	 * 微信返回的签名
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
	 * 微信退款单号
	 */
	@XmlElement(name = "refund_id")
	private String refund_id;
	
	/**
	 * 业务结果
	 * SUCCESS/FAIL
	 */
	@XmlElement(name = "result_code")
	private String result_code;
	
	/**
	 * 错误代码
	 */
	@XmlElement(name = "err_code")
	private String err_code;
	
	/**
	 * 错误代码描述
	 */
	@XmlElement(name = "err_code_des")
	private String err_code_des;
	
	/**
	 * ORIGINAL—原路退款	BALANCE—退回到余额
	 */
	@XmlElement(name = "refund_channel")
	private String refund_channel;
	
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
	@XmlElement(name = "fee_type")
	private String fee_type;
	
	/**
	 * 现金支付金额，单位为分
	 */
	@XmlElement(name = "cash_fee")
	private String cash_fee;
	
	/**
	 * 现金退款金额，单位为分
	 */
	@XmlElement(name = "cash_refund_fee")
	private String cash_refund_fee;
	
	/**
	 * 代金券或立减优惠退款金额=订单金额-现金退款金额，注意：立减优惠金额不会退回
	 */
	@XmlElement(name = "coupon_refund_fee")
	private String coupon_refund_fee;
	
	/**
	 * 代金券或立减优惠使用数量
	 */
	@XmlElement(name = "coupon_refund_count")
	private String coupon_refund_count;
	
	/**
	 * 代金券或立减优惠ID
	 */
	@XmlElement(name = "coupon_refund_id")
	private String coupon_refund_id;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

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

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
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

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getRefund_channel() {
		return refund_channel;
	}

	public void setRefund_channel(String refund_channel) {
		this.refund_channel = refund_channel;
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

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(String cash_fee) {
		this.cash_fee = cash_fee;
	}

	public String getCash_refund_fee() {
		return cash_refund_fee;
	}

	public void setCash_refund_fee(String cash_refund_fee) {
		this.cash_refund_fee = cash_refund_fee;
	}

	public String getCoupon_refund_fee() {
		return coupon_refund_fee;
	}

	public void setCoupon_refund_fee(String coupon_refund_fee) {
		this.coupon_refund_fee = coupon_refund_fee;
	}

	public String getCoupon_refund_count() {
		return coupon_refund_count;
	}

	public void setCoupon_refund_count(String coupon_refund_count) {
		this.coupon_refund_count = coupon_refund_count;
	}

	public String getCoupon_refund_id() {
		return coupon_refund_id;
	}

	public void setCoupon_refund_id(String coupon_refund_id) {
		this.coupon_refund_id = coupon_refund_id;
	}

}
