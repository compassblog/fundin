package com.fundin.web.wechat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fundin.pay.wx.WXPayConstants;
import com.fundin.utils.https.SSLUtils;

public class WXAccessToken {

	private static String access_token;
	private static final String REFRESH_TOKEN_URL = WXPayConstants.WX_ACCESS_TOKEN_URL + "?grant_type=client_credential&appid=" 
			+ WXPayConstants.WX_APP_ID + "&secret=" + WXPayConstants.WX_API_KEY;
	
	private static final ScheduledExecutorService WORKER = Executors.newSingleThreadScheduledExecutor(
			new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true);
					t.setName("WXAccessToken Worker Thread");
					t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
						@Override
						public void uncaughtException(Thread t, Throwable e) {
							WXPayConstants.WXJsApiLog.error(e.getMessage(), e);
						}
					});
					return t;
				}
	});
	
	static {
		refreshAccessToken();
		if (StringUtils.isBlank(access_token)) {
			throw new RuntimeException("start refreshAccessToken() get blank token!");
		}
		WORKER.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				refreshAccessToken();
				WXJsApiConfig.notifyRefresh();
			}
		}, 1, 1, TimeUnit.HOURS);
	}
	
	public static void refreshAccessToken() {
		try {
			String ret = SSLUtils.post(REFRESH_TOKEN_URL, "");
			JSONObject retObj = JSONObject.parseObject(ret);
			
			String retToken = retObj.getString("access_token");
			if (StringUtils.isNotBlank(retToken)) {
				access_token = retToken;
				WXPayConstants.WXJsApiLog.info("refreshAccessToken success...{}", retObj);
				return ;
			}
			
			String retErr = retObj.getString("errcode");
			if (StringUtils.isNotBlank(retErr)) {
				WXPayConstants.WXJsApiLog.error("refreshAccessToken error...{}", retObj);
			}
		} catch (Exception e) {
			WXPayConstants.WXJsApiLog.error("refreshAccessToken error...", e);
		}
	}
	
	public static String getAccessToken() {
		return access_token;
	}
	
	public static void main(String args[]){
		
	}
}
