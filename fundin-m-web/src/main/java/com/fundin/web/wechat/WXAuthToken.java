package com.fundin.web.wechat;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fundin.pay.wx.WXPayConstants;
import com.fundin.utils.common.MD5Util;
import com.fundin.utils.common.StringUtil;
import com.fundin.utils.https.SSLUtils;

public class WXAuthToken {

	private static final String AUTH_TOKEN_URL = WXPayConstants.WX_AUTH_TOKEN_URL + "?appid=" + WXPayConstants.WX_APP_ID 
			+ "&secret=" + WXPayConstants.WX_API_KEY + "&grant_type=authorization_code&code=";
	
	public static String getUserInfoByCode(String code) {
		try {
			String ret = SSLUtils.post(AUTH_TOKEN_URL + code, "");
			JSONObject retObj = JSONObject.parseObject(ret);
			
			String openid = retObj.getString("openid");
			String access_token = retObj.getString("access_token");
			
			String retErr = retObj.getString("errcode");
			if (StringUtils.isNotBlank(retErr)) {
				WXPayConstants.WXJsApiLog.error("getUserInfoByCode retErr...{}", retObj);
			}
			
			String userinfo = SSLUtils.post(WXPayConstants.WX_USER_INFO_URL + 
					"?access_token=" + access_token + "&openid=" + openid 
					+ "&lang=zh_CN", "");
			
			JSONObject userinfoObj = JSONObject.parseObject(userinfo);
			String userInfoErr = userinfoObj.getString("errcode");
			if (StringUtils.isNotBlank(userInfoErr)) {
				WXPayConstants.WXJsApiLog.error("getUserInfoByCode userInfoErr...{}", userinfoObj);
			}
			
			return userinfo;
		} catch (Exception e) {
			WXPayConstants.WXJsApiLog.error("getUserInfoByCode error...", e);
		}
		return null;
	}
	
	public static String getOpenidByCode(String code) {
		try {
			String ret = SSLUtils.post(AUTH_TOKEN_URL + code, "");
			JSONObject retObj = JSONObject.parseObject(ret);
			
			String openid = retObj.getString("openid");
			if (StringUtils.isNotBlank(openid)) {
				return openid;
			}
			
			String retErr = retObj.getString("errcode");
			if (StringUtils.isNotBlank(retErr)) {
				WXPayConstants.WXJsApiLog.error("getOpenidByCode error...{}", retObj);
			}
		} catch (Exception e) {
			WXPayConstants.WXJsApiLog.error("getOpenidByCode error...", e);
		}
		return null;
	}
	
	public static WXPay obtainWXPay(String prepay_id) {
		WXPay pay = new WXPay();
		pay.setTimeStamp(System.currentTimeMillis() / 1000);
		pay.setNonceStr(StringUtil.generateRandomString(32));
		pay.setPkg("prepay_id=" + prepay_id);
		pay.setPaySign(generateSignature(pay));
		return pay;
	}
	
	private static String generateSignature(WXPay pay) {
		String input = "appId=" + WXPayConstants.WX_APP_ID + "&nonceStr=" + pay.getNonceStr() 
				+ "&package=" + pay.getPkg() + "&signType=" + pay.getSignType() 
				+ "&timeStamp=" + pay.getTimeStamp() + "&key=" + WXPayConstants.WX_API_KEY;
		return MD5Util.MD5(input).toUpperCase();
	}

	public static class WXPay {
		private long timeStamp;
		private String nonceStr;
		private String pkg;
		private String signType = "MD5";
		private String paySign;
		public long getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getNonceStr() {
			return nonceStr;
		}
		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}
		public String getPkg() {
			return pkg;
		}
		public void setPkg(String pkg) {
			this.pkg = pkg;
		}
		public String getSignType() {
			return signType;
		}
		public void setSignType(String signType) {
			this.signType = signType;
		}
		public String getPaySign() {
			return paySign;
		}
		public void setPaySign(String paySign) {
			this.paySign = paySign;
		}
	}
	
}
