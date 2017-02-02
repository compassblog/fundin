package com.fundin.web.wechat;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fundin.pay.wx.WXPayConstants;
import com.fundin.utils.common.StringUtil;
import com.fundin.utils.https.SSLUtils;
import com.fundin.utils.security.Digests;

public class WXJsApiConfig {

	private static String jsapi_ticket;
	private static final String REFRESH_JS_API_URL = WXPayConstants.WX_JS_API_URL + "?type=jsapi&access_token=";
	public static final String[] JS_API_LIST = new String[]
			{"onMenuShareTimeline", "onMenuShareAppMessage", "chooseWXPay"};
	
	static {
		refreshTicket();
		if (StringUtils.isBlank(jsapi_ticket)) {
			throw new RuntimeException("start refreshTicket() get blank ticket!");
		}
	}

	private static void refreshTicket() {
		try {
			String ret = SSLUtils.post(REFRESH_JS_API_URL + WXAccessToken.getAccessToken(), "");
			JSONObject retObj = JSONObject.parseObject(ret);
			
			String retTicket = retObj.getString("ticket");
			if (StringUtils.isNotBlank(retTicket)) {
				jsapi_ticket = retTicket;
				WXPayConstants.WXJsApiLog.info("refreshTicket success...{}", retObj);
				return ;
			}
			
			String retErr = retObj.getString("errcode");
			if (StringUtils.isNotBlank(retErr)) {
				WXPayConstants.WXJsApiLog.error("refreshTicket error...{}", retObj);
			}
		} catch (Exception e) {
			WXPayConstants.WXJsApiLog.error("refreshTicket error...", e);
		}
	}
	
	public static void notifyRefresh() {
		refreshTicket();
	}
	
	public static JsApiConfig getJsApiConfig(String url, String[] jsApiList) {
		JsApiConfig config = new JsApiConfig();
		config.setAppId(WXPayConstants.WX_APP_ID);
		config.setNonceStr(StringUtil.generateRandomString(16));
		config.setTimestamp(System.currentTimeMillis() / 1000);
		config.setSignature(generateSignature(url, config));
		config.setJsApiList(jsApiList);
		return config;
	}
	
	private static String generateSignature(String url, JsApiConfig config) {
		String input = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + config.getNonceStr()
				+ "&timestamp=" + config.getTimestamp() + "&url=" + url;
		return Digests.byteToString(Digests.sha1(input.getBytes()));
	}

	public static class JsApiConfig {
		private String appId;
		private String nonceStr;
		private long timestamp;
		private String signature;
		private String[] jsApiList;
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		public String getNonceStr() {
			return nonceStr;
		}
		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
		public String[] getJsApiList() {
			return jsApiList;
		}
		public void setJsApiList(String[] jsApiList) {
			this.jsApiList = jsApiList;
		}
		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}
	
	public static void main(String args[]) {
		System.out.println(getJsApiConfig("http://m.fundin.cn/proj/16", JS_API_LIST));
	}
}
