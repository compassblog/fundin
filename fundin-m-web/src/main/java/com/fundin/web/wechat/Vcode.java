package com.fundin.web.wechat;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fundin.utils.common.StringUtil;
import com.fundin.utils.http.HttpUtils;
import com.fundin.utils.http.HttpUtils.RequestBuilder;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class Vcode {

	private static final Logger LOG = LoggerFactory.getLogger(Vcode.class);
	
	private static final Cache<String, String> codeCache = 
			CacheBuilder.newBuilder().maximumSize(1000)
				.expireAfterWrite(10, TimeUnit.MINUTES).build();
	private static final String SMS_URL = "http://v1.avatardata.cn/Sms/Send";
	
	public static void createCode(String token, String phone) {
		String code = StringUtil.generateRandomCode(4);
		codeCache.put(token, code);
		sendSmsCode(code, phone);
	}

	private static void sendSmsCode(final String code, final String phone) {
		try {
			HttpUtils.sendRequest(new RequestBuilder() {
				public HttpRequestBase buildRequest() {
					HttpGet request = new HttpGet(SMS_URL 
							+ "?key=9924f592dc1e42eda44e421ca1e28603&mobile=" + phone 
							+ "&templateId=b36ec12665c54f269ed7f0acb8aeec25&param=" + code);
					return request;
				}
			});
		} catch (Exception e) {
			LOG.error("Vcode sendSmsCode error !", e);
		}
	}
	
	public static boolean checkCode(String token, String code) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(code)) {
			return false;
		}
		
		try {
			String origCode = codeCache.get(token, new Callable<String>() {
				@Override
				public String call() throws Exception {
					return null;
				}
			});
			
			if (null == origCode || ! code.equals(origCode)) {
				return false;
			}
		} catch (Exception e) {
			LOG.error("Vcode checkCode error !", e);
		}
		
		return true;
	}
	
}
