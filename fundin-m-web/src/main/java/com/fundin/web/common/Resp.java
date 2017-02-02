package com.fundin.web.common;

public class Resp {

	private static final int SUCCESS = 1;
	private static final int FAIL = 0;
	
	private int code;
	private String msg;
	private Object data;
	
	public static Resp succ(Object data) {
		return new Resp().setCode(SUCCESS).setData(data);
	}
	
	public static Resp fail(String msg) {
		return new Resp().setCode(FAIL).setMsg(msg);
	}
	
	public int getCode() {
		return code;
	}
	
	public Resp setCode(int code) {
		this.code = code;
		return this;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public Resp setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return data;
	}

	public Resp setData(Object data) {
		this.data = data;
		return this;
	}
	
}