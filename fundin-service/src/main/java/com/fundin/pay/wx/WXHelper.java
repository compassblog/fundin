package com.fundin.pay.wx;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.fundin.utils.common.Clock;
import com.fundin.utils.common.DateUtil;
import com.google.common.collect.Maps;

public final class WXHelper {

	private static <T> Map<String, Object> obtainFieldsMapNotNull(T t) {
		Class<?> clz = t.getClass();
		Field[] fields = clz.getDeclaredFields();
		Map<String, Object> fieldsMap = Maps.newHashMap();
		
		for (Field field : fields) {
			String key = field.getName();
			Object value = null;
			
			try {
				field.setAccessible(true);
				value = field.get(t);
//				value = Reflections.invokeGetter(t, key);
			} catch (Exception e) {
				throw new RuntimeException("obtainFieldsMapNotNull field get error !", e);
			}
			
			if (null != value) {
				fieldsMap.put(key, value);
			}
		}
		
		return fieldsMap;
	}
	
	public static <T> String generateSign(T t) {
		Map<String, Object> fieldsMap = obtainFieldsMapNotNull(t);
		TreeMap<String, Object> sortedFieldsMap = new TreeMap<String, Object>(fieldsMap);
		
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> fieldsEntry : sortedFieldsMap.entrySet()) {
			sb.append(fieldsEntry.getKey()).append("=").
				append(fieldsEntry.getValue()).append("&");
		}
		sb.deleteCharAt(sb.lastIndexOf("&"));
		
		return sb.toString();
	}
	
	public static String generateTradeNo() {
		String tradeNo = DateUtil.dateFormat(Clock.DEFAULT.getCurrentDate(), 
				DateUtil.DATEFORMAT_FOR_ORDER);
		if (StringUtils.isBlank(tradeNo)) {
			throw new RuntimeException("generateTradeNo error !");
		}
		return tradeNo;
	}
	
	private static int projStrLength = 10;
	private static String ZeroStr = "0";
	
	public static String generateProductId(Long projId) {
		StringBuilder sb = new StringBuilder();
		
		String projIdStr = String.valueOf(projId);
		int projStrLen = projStrLength - projIdStr.length();
		for (int i = 0;i < projStrLen;i++) {
			sb.append(ZeroStr);
		}
		sb.append(projIdStr);
		
		return sb.toString();
	}
	
}
