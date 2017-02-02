package com.fundin.pay.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WXPayConstants {

	public static final String WX_API_KEY = "2dd763b2d40037c54ebf2a39daace466";
	public static final String WX_APP_ID = "wx4f5914d98501fec9";
	public static final String WX_MCH_ID = "1295319601";
	
	public static final String WX_DEVICE_INFO = "WEB";
	public static final String WX_TRADE_NATIVE = "NATIVE";
	public static final String WX_TRADE_JS_API = "JSAPI";
	public static final String WX_FEE_TYPE = "CNY";
	public static final int PAY_WAY_OF_WX = 1;
	
	public static final String WX_CREATE_IP = "115.159.69.130";
	
	public static final String WX_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String WX_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	public static final String WX_NOTIFY_URL = "http://www.fundin.cn/pay/wxPayNotify";
	
	public static final String WX_RETURN_CODE = "SUCCESS";
	public static final String WX_RETURN_MSG = "OK";
	public static final String WX_RESULT_CODE = "SUCCESS";
	
	public static final int WX_ORDER_STATUS_UNPAYED = 0;
	public static final int WX_ORDER_STATUS_PAYED = 1;
	public static final int WX_ORDER_STATUS_REFUNDED = 2;
	
	public static final String WX_KEY_STORE_PATH = "/disk/server/keystore/" + WX_MCH_ID + ".p12";
	
	public static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	public static final String WX_JS_API_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
	public static final String WX_AUTH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static final String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	
	public static final Logger WXJsApiLog = LoggerFactory.getLogger("WXJsApiLog");
}
