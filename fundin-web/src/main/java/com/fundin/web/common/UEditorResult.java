package com.fundin.web.common;

import com.alibaba.fastjson.JSON;

public class UEditorResult {

	private static String SUCCESS = "SUCCESS";
	private static String FAILED = "FAILED";
	
	private String url;
	private String state;
	
	public static String success(String fileUrl){
		UEditorResult result = new UEditorResult();
		result.setState(SUCCESS);
		result.setUrl(fileUrl);
		return JSON.toJSONString(result);
	}
	
	public static String failed(){
		UEditorResult result = new UEditorResult();
		result.setState(FAILED);
		return JSON.toJSONString(result);
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
}
